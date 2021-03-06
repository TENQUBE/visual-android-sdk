package tenqube.transmsparser.core;

import android.content.Context;
import android.database.SQLException;
import android.text.TextUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tenqube.transmsparser.constants.Constants;
import tenqube.transmsparser.model.RegData;
import tenqube.transmsparser.model.SMS;
import tenqube.transmsparser.util.Validator;
import tenqube.transmsparser.util.LogUtil;

import static tenqube.transmsparser.core.DateUtil.getFinishDate;
import static tenqube.transmsparser.core.Utils.getMatcher;
import static tenqube.transmsparser.core.Utils.transformKeyword;
import static tenqube.transmsparser.util.LogUtil.makeLogTag;


class RegHandler {

    private static final String TAG = LogUtil.makeLogTag(RegHandler.class);

    private Context mContext;

    public RegHandler(Context context) {
        this.mContext = context;
    }

    TransactionTableData getTransactionWithReg(ArrayList<RegData> regDatas, SMS sms){

        TransactionTableData parsedTran;
        Matcher matcher;
        Pattern pattern;

        String newFullSms  = Utils.transformFullSMS(sms.getFullSms());
        if(Validator.invalidStr(Validator.FULL_SMS, newFullSms))
            return null;

        if(regDatas == null) regDatas = new ArrayList<>();
        LogUtil.LOGI(TAG, "regDatas size" + regDatas.size());

        for (RegData regData : regDatas) {

            pattern = Pattern.compile(regData.regExpression, Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE);
            matcher = pattern.matcher(newFullSms);

            if(matcher.matches()) {

                LogUtil.LOGI(TAG,"Matched regId" + regData.regId);

                parsedTran = new TransactionTableData();
                parsedTran.regId = regData.regId;
                parsedTran.userPriority = regData.userPriority;
                parsedTran.identifier = getIdentifier();
                parsedTran.isSuccess = Constants.YES;
                parsedTran.isCurrentTran = true;
                parsedTran.categoryCode = 10;
                parsedTran.memo = "";

                parsedTran.smsId = sms.getSmsId();
                parsedTran.fullSms = sms.getFullSms();
                parsedTran.smsDate = sms.getSmsDate();
                parsedTran.smsType = sms.getSmsType();
                parsedTran.sender = sms.getSender();
                if(Validator.invalidStr(Validator.SENDER, parsedTran.sender)){
                    LogUtil.LOGI(TAG,"invalidStr(SENDER)" + parsedTran.sender);
                    return null;
                }

                CardTableData parsedCard = new CardTableData();

                //cardName
                String parsedCardName = getCardName(getParsedData(regData.cardName, matcher));
                parsedCard.cardName = TextUtils.isEmpty(parsedCardName) || parsedCardName.contains("null") ?
                        Constants.KEYWORD_EMPTY : parsedCardName.trim();
                if(Validator.invalidStr(Validator.CARD_NAME, parsedCard.cardName)) {
                    LogUtil.LOGI(TAG,"invalidStr(CARD_NAME)" + parsedCard.cardName);
                    return null;
                }
                //cardNum
                parsedCard.cardNum = getCardNum(regData.cardNum, matcher);

                //cardType
                parsedCard.cardType = getCardType(regData.cardType, matcher);

                //cardSubtype
                parsedCard.cardSubType = getCardSubType(regData.cardSubType, matcher);

                //balance
                parsedTran.parsedBalance = getBalance(regData.balance, matcher);

                if(parsedTran.parsedBalance != null)
                    parsedCard.balance = parsedTran.parsedBalance;

                //insert Card
                parsedTran.cardTableData = parsedCard;

                //?????? ?????????
                String isBank = parsedTran.cardTableData.cardType == Constants.CardType.BANK_ACCOUNT.ordinal() ? "1" : "0";
                parsedTran.originInfos = parsedTran.sender + ";" + isBank + ";" + parsedTran.cardTableData.cardName;

                //dwType
                parsedTran.dwType = getDWType(regData.dwType, matcher);


                //spentMoney
                parsedTran.spentMoney = getSpentMoney(regData.spentMoney, matcher);
                if(parsedTran.spentMoney == 0) {
                    LogUtil.LOGI(TAG,"parsedTran.spentMoney" + parsedTran.spentMoney);
                    return null;
                }

                //isCancel
                if(isCancel(regData.isCancel, matcher))
                    parsedTran.spentMoney *= -1;

                //spentDate
                String parsedSpentDate = getParsedData(regData.spentDate, matcher);

                parsedTran.spentDate = TextUtils.isEmpty(parsedSpentDate) ?
                        sms.getSmsDate() : Utils.transformDate(DateUtil.convertStringToCalendarFULL(sms.getSmsDate()), parsedSpentDate);

                //?????? ??????????????? ?????? ?????? ??????
                if(parsedTran.spentMoney < 0) {
                    parsedTran.spentDate = Utils.getCanceledSpentDate(DateUtil.convertStringToCalendarFULL(sms.getSmsDate()),
                            DateUtil.convertStringToCalendarFULL(parsedTran.spentDate));
                }
                if(!Validator.isDate(parsedTran.spentDate)) {
                    LogUtil.LOGI(TAG,"invalidStr(spentDate)" + parsedTran.spentDate);
                    return null;
                }

                //installmentCount
                parsedTran.installmentCount = getInstallmentCount(regData.installmentCount, matcher);

                //finishDate
                parsedTran.finishDate = getFinishDate(parsedTran.spentDate, parsedTran.installmentCount);

                String parsedKeyword = parseKeyword(regData, sms, matcher);

                parsedTran.keyword = TextUtils.isEmpty(parsedKeyword) || parsedKeyword.contains("null") ?
                        Constants.KEYWORD_EMPTY : parsedKeyword;

                parsedTran.keyword = transformKeyword(parsedTran.keyword);
                if(Validator.invalidStr(Validator.KEYWORD, parsedTran.keyword)) {
                    LogUtil.LOGI(TAG,"invalidStr(KEYWORD)" + parsedTran.keyword);
                    return null;
                }


                parsedTran.categoryCode = parsedTran.dwType == Constants.DWType.DEPOSIT.ordinal() ? 90 : 10;


                //currency
                parsedTran.currency = getCurrency(regData.currency, matcher);

                if(!TextUtils.isEmpty(parsedTran.currency)) {
                    parsedTran.memo = parsedTran.currency +" "+ Utils.transformSpentMoney(parsedTran.spentMoney);
                }
                //userName
                String userName = getUserName(regData.userName, matcher);
                try {

                    if(!TextUtils.isEmpty(userName)) {
                        userName = userName.replace("(", "").replace(")", "").replace("*", "\\*");
                        parsedTran.fullSms = parsedTran.fullSms.replaceFirst(userName, "qlip");
                    }

                } catch (Exception e) {
                   e.printStackTrace();
                }
                return parsedTran;
            }
        }
        return null;

    }

    private String parseKeyword(RegData regData, SMS sms, Matcher matcher) {
        //keyword

        if(!TextUtils.isEmpty(sms.getTitle()) && sms.getTitle().length() > 1) {  // ????????? ?????? ???????????? 50??? ????????? ???????????? ????????????
            if("12345678901234567890123456789012345678901234567890title".equals(regData.keyword)) {

                return sms.getTitle();

            } else if(regData.keyword.contains("title,")) {
                int titleIndex = regData.keyword.indexOf("title,");
                String regs = regData.keyword.substring(titleIndex + 6);
                return getParsedData(regs, matcher);
            } else {
                return getParsedData(regData.keyword, matcher);
            }

        } else {

            return getParsedData(regData.keyword, matcher);

        }
    }

    private String getCardName(String cardName) {

        if(TextUtils.isEmpty(cardName))
            return "";

        return cardName.replace("??????", "").replace("-","");

    }

    private String getCardNum(String cardNum, Matcher matcher) {

        return !TextUtils.isEmpty(cardNum)&& Validator.isNumber(cardNum)?
                Utils.getCardNumUntilFour(getMatcher(Integer.parseInt(cardNum), matcher)).trim():
                "";

    }

    private int getCardType(String cardType, Matcher matcher) {
        //CardType

        return !TextUtils.isEmpty(cardType)&&Validator.isNumber(cardType)?
                getCardTypeStr(getMatcher(Integer.parseInt(cardType), matcher))
                :
                getCardTypeStr(cardType);

    }

    private int getCardTypeStr(String cardType) {

        if (TextUtils.isEmpty(cardType))
            return Constants.CardType.CARD.ordinal();

        if ("??????".equals(cardType)) {

            return Constants.CardType.CHECK.ordinal();

        } else if ("??????".equals(cardType)) {

            return Constants.CardType.BANK_ACCOUNT.ordinal();

        } else {

            return Constants.CardType.CARD.ordinal();

        }
    }

    private int getCardSubType(String cardSubType, Matcher matcher) {
        //CardSubType

        return !TextUtils.isEmpty(cardSubType)&&Validator.isNumber(cardSubType)?
                getCardSubTypeStr(getMatcher(Integer.parseInt(cardSubType), matcher))
                :
                getCardSubTypeStr(cardSubType);

    }

    private int getCardSubTypeStr(String cardSubType) {

        if (TextUtils.isEmpty(cardSubType))
            return Constants.CardSubType.NORMAL.ordinal();

        if ("??????".equals(cardSubType)) {

            return Constants.CardSubType.CORPORATION.ordinal();

        } else if ("??????".equals(cardSubType)) {

            return Constants.CardSubType.FAMILY.ordinal();

        } else {

            return Constants.CardSubType.NORMAL.ordinal();

        }
    }

    private Double getBalance(String balance, Matcher matcher) {


        if(!TextUtils.isEmpty(balance)&&Validator.isNumber(balance)) {
            String balanceStr = getMatcher(Integer.parseInt(balance), matcher);
            if(!TextUtils.isEmpty(balanceStr)) {

                try {

                    return Double.parseDouble(balanceStr.replace(",","").replace(" ",""));

                } catch (NumberFormatException e) {
                    return null;
                }
            }

        }

        return null;
    }

    private double getSpentMoney(String spentMoney, Matcher matcher) {

        LogUtil.LOGI(TAG, "getSpentMoney1" + spentMoney);

        if(!TextUtils.isEmpty(spentMoney)&&Validator.isNumber(spentMoney)) {
            String spentMoneyStr = getMatcher(Integer.parseInt(spentMoney), matcher);

            LogUtil.LOGI(TAG, "getSpentMoney2" + spentMoneyStr);

            if(TextUtils.isEmpty(spentMoneyStr))
                return 0;

            if(spentMoneyStr.contains("??????")) {
                return Utils.transformSpentMoney(spentMoneyStr);
            } else {

                try {
                    LogUtil.LOGI(TAG, "getSpentMoney3" + spentMoneyStr.replace(",","").replace(" ","").replace("O","0").trim());
                    return Double.parseDouble(spentMoneyStr.replace(",","").replace(" ","").replace("O","0").trim());

                } catch (NumberFormatException e) {
                    return 0;
                }
            }

        }
        return 0;

    }

    private int getInstallmentCount(String installmentCount, Matcher matcher) {


        if(!TextUtils.isEmpty(installmentCount) && Validator.isNumber(installmentCount)) {
            String installmentStr = getMatcher(Integer.parseInt(installmentCount), matcher);
            if(TextUtils.isEmpty(installmentStr))
                return 1;

            try {

                return Integer.parseInt(installmentStr);

            } catch (NumberFormatException e) {
                return 1;
            }

        }

        return 1;

    }

    private int getDWType(String dwType, Matcher matcher) {


        return !TextUtils.isEmpty(dwType)&&Validator.isNumber(dwType)?
                getDWTypeStr(getMatcher(Integer.parseInt(dwType), matcher))
                :
                getDWTypeStr(dwType);

    }


    private int getDWTypeStr(String dwType) {

        if (TextUtils.isEmpty(dwType))
            return Constants.DWType.WITHDRAW.ordinal();

        return dwType.equals("??????") || dwType.equals("???") ?
                Constants.DWType.DEPOSIT.ordinal()
                :
                Constants.DWType.WITHDRAW.ordinal();
    }


    private boolean isCancel(String isCancel, Matcher matcher) {

        return !TextUtils.isEmpty(isCancel)&&Validator.isNumber(isCancel)?
                isCancel(getMatcher(Integer.parseInt(isCancel), matcher))
                :
                isCancel(isCancel);
    }


    private boolean isCancel(String isCancel) {

        return !TextUtils.isEmpty(isCancel) && ("???".equals(isCancel) || isCancel.contains("??????") || isCancel.contains("??????")) ;
    }

    private String getCurrency(String currency, Matcher matcher) {

        return !TextUtils.isEmpty(currency)&&Validator.isNumber(currency)?
                Utils.transformCurrency(getMatcher(Integer.parseInt(currency), matcher))
                :
                Utils.transformCurrency(currency);

    }

    private String getUserName(String userName, Matcher matcher) {

        return !TextUtils.isEmpty(userName)&&Validator.isNumber(userName)?
                getMatcher(Integer.parseInt(userName), matcher)
                :
                "";
    }

    private String getParsedData(String regStr, Matcher matcher) {

        StringBuilder valueStr = new StringBuilder();
        if (!TextUtils.isEmpty(regStr)) {
            if (regStr.contains(",")) {
                String[] subMapping =  regStr.split(",");
                for (String sub : subMapping) {
                    if (!TextUtils.isEmpty(sub)) {

                        valueStr.append(Validator.isNumber(sub) ?
                                getMatcher(Integer.parseInt(sub), matcher)
                                :
                                sub) ;
                    }
                }
            } else {

                valueStr.append(Validator.isNumber(regStr) ?
                        getMatcher(Integer.parseInt(regStr), matcher)
                        :
                        regStr);
            }
        }

        return valueStr.toString();

    }


    boolean isDuplicate(@NotNull String duplTranInfo, @NotNull String parsedTranInfo){


        try {
            String[] dupleTranInfos = duplTranInfo.split(";");
            String[] parsedTranInfos = parsedTranInfo.split(";");

            String duplSender = dupleTranInfos[0];
            String duplIsBank = dupleTranInfos[1];
            String duplCardName = dupleTranInfos[2];

            String parsedSender = parsedTranInfos[0];
            String parsedIsBank = parsedTranInfos[1];
            String parsedCardName = parsedTranInfos[2];

            return  (!(duplSender.equals(parsedSender) && duplIsBank.equals(parsedIsBank)))
                    &&
                    (isPay(duplCardName, parsedCardName) ||
                            Utils.matchingCardName(
                                    duplCardName,
                                    parsedCardName,
                                    duplIsBank,
                                    parsedIsBank));

        } catch (Exception e){
            return  false;
        }

    }

    private boolean isPay(String duplCardName, String parsedCardName) {
        return Constants.SAMSUNG_PAY.equals(duplCardName) ||
                Constants.SAMSUNG_PAY.equals(parsedCardName) ||
                Constants.LG_PAY.equals(duplCardName) ||
                Constants.LG_PAY.equals(parsedCardName);

    }

    /**
     * ????????? ?????? ??????????????? ?????? ???????????? ???????????????.
     *
     * 1. ????????? ?????? (identifier, balance, isOffset, categoryCode, dwType, installmentCount, keyword, Card, originInfo)
     *
     *
     * @param duplicationTran ????????? Transaction
     * @return ????????? Transaction
     */
    TransactionTableData combineTran(@NotNull TransactionTableData duplicationTran, @NotNull TransactionTableData parsedTran){

        parsedTran.cardTableData.balance = duplicationTran.cardTableData.balance;
        parsedTran.isOffset = duplicationTran.isOffset;
        parsedTran.categoryCode = duplicationTran.categoryCode == 88 ||
                duplicationTran.categoryCode == 98 ?
                duplicationTran.categoryCode
                :
                parsedTran.categoryCode;

        parsedTran.isDuplicate = Constants.YES;
        parsedTran.identifier = duplicationTran.identifier;

        parsedTran = combineDwType(duplicationTran, parsedTran);
        parsedTran.installmentCount = combineInstallmentCount(duplicationTran.installmentCount, parsedTran.installmentCount);
        parsedTran.keyword = combineKeyword(duplicationTran.keyword, parsedTran.keyword);
        parsedTran.cardTableData = combineCard(duplicationTran.cardTableData, parsedTran.cardTableData, parsedTran.smsType);
        parsedTran.originInfos = combineOriginInfo(duplicationTran.originInfos, parsedTran.originInfos);
        return parsedTran;

    }

    private TransactionTableData combineDwType(@NotNull TransactionTableData duplicationTran, @NotNull TransactionTableData parsedTran) {

        if (duplicationTran.dwType != parsedTran.dwType) {
            if (duplicationTran.dwType == Constants.DWType.WITHDRAW.ordinal()) {

                parsedTran.spentMoney = duplicationTran.spentMoney;
                parsedTran.dwType = duplicationTran.dwType;
            }
        }

        return parsedTran;
    }

    private int combineInstallmentCount(int duplInstallmentCount, int parsedInstallmentCount) {

        if (parsedInstallmentCount < duplInstallmentCount) {
            return duplInstallmentCount;
        }
        return parsedInstallmentCount;

    }

    private String combineKeyword(String duplKeyword, String parsedKeyword) {

        if (
                (TextUtils.isEmpty(parsedKeyword) ||
                Constants.KEYWORD_EMPTY.equals(parsedKeyword) ||
                Constants.NONE.equals(parsedKeyword))
                        ||
                (!TextUtils.isEmpty(duplKeyword) &&
                !Constants.KEYWORD_EMPTY.equals(duplKeyword) &&
                !Constants.NONE.equals(duplKeyword) &&
                parsedKeyword.length() < duplKeyword.length())
                ) {

            return duplKeyword;

        }

        return parsedKeyword;
    }

    private CardTableData combineCard(@NotNull CardTableData duplicationCard, @NotNull CardTableData parsedCard, int parsedSMSType) {

        //?????? Tran??? ????????? ?????????????????? ???????????? ??????

        if (Constants.SAMSUNG_PAY.equals(duplicationCard.cardName) || Constants.LG_PAY.equals(duplicationCard.cardName))
            return parsedCard;

        //????????? Tran??? ????????? ?????????????????? ?????? ????????? ??????
        if (Constants.SAMSUNG_PAY.equals(parsedCard.cardName) || Constants.LG_PAY.equals(parsedCard.cardName)) {
            return duplicationCard;
        }

        if (parsedCard.cardType == duplicationCard.cardType) {

            if (parsedCard.cardName.length() < parsedCard.cardName.length()) {

                return duplicationCard;

            } else if (parsedCard.cardName.length() == duplicationCard.cardName.length() &&
                    parsedSMSType == Constants.SMSType.NOTIFICATION.ordinal()) {

                return duplicationCard;
            }

        } else if (parsedCard.cardType == Constants.CardType.BANK_ACCOUNT.ordinal()) {

            return duplicationCard;

        }

        return parsedCard;
    }


    private String combineOriginInfo(@NotNull String duplOriginInfo, @NotNull String parsedInfo) {

        String[] parsedOriginInfos = parsedInfo.split(";");
        String[] duplOriginInfos = duplOriginInfo.split(";");

        if (parsedOriginInfos.length > 1 &&
                duplOriginInfos.length > 1) {
            String parsedIsBank = parsedOriginInfos[1];
            String duplIsBank = duplOriginInfos[1];
            if ("1".equals(parsedIsBank) ||
                    parsedIsBank.equals(duplIsBank)) {
                return duplOriginInfo;
            }
        }
        return parsedInfo;
    }

    /**
     *
     * ????????? ???????????? ????????? ????????? ?????? ????????????????????? ????????? ?????????
     *
     * @param offsetTran ????????? Transaction
     * @param parsedTran ????????? Transaction
     * @return ????????? ????????? Transaction
     */
    TransactionTableData combineTranWithOffset(@NotNull TransactionTableData offsetTran, @NotNull TransactionTableData parsedTran) {

        if(parsedTran.spentMoney < 0) { // ????????? ????????? ?????? ????????? ?????? ?????? ????????? ??????.
            parsedTran.cardTableData = offsetTran.cardTableData;
            parsedTran.isOffset = Constants.YES;
            parsedTran.categoryCode = offsetTran.categoryCode;

        } else { // ?????? ????????? ?????? ????????? ??????
            offsetTran.cardTableData = parsedTran.cardTableData;
            offsetTran.isOffset = Constants.YES;
        }
        return parsedTran;

    }


    TransactionTableData combineTranWithMovingAsset(@NotNull TransactionTableData tran) {

        tran.categoryCode = tran.dwType == Constants.DWType.DEPOSIT.ordinal() ?
                98 : 88;

        return tran;

    }


    /**
     * ?????? ???????????? ??????
     *
     * 1. ????????? ????????? ???????????? ????????? ???????????? ??????
     * 2. ????????? ????????? ???????????? dwType??? ?????? ?????? ?????? +- ??????
     * @param tran
     * @return
     * @throws SQLException
     */
    TransactionTableData updateBalance(TransactionTableData tran) throws SQLException {

        //????????? ????????? ????????? ??????
        if (tran.parsedBalance != null) {

            tran.cardTableData.balance = tran.parsedBalance;

            return tran;
        }

        if (tran.isDuplicate == Constants.NO) {

            tran.cardTableData.balance = tran.dwType == Constants.DWType.DEPOSIT.ordinal() ?
                    tran.cardTableData.balance + tran.spentMoney
                    :
                    tran.cardTableData.balance - tran.spentMoney;

        }

        return tran;
    }

    /**
     * Transaction Pk ????????? ??????
     *
     * @return identifier
     */
    private String getIdentifier(){

        try {

            String androidDeviceId = android.provider.Settings.Secure.getString(mContext.getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);
            if (androidDeviceId == null) {
                androidDeviceId = "NoAndroidId";
            }
            String end = Calendar.getInstance().getTimeInMillis() + "";

            String start = androidDeviceId.hashCode() + "";
            if (start.startsWith("-")) {
                start = start.substring(1, start.length());
            }
            start = start.length()>5?start.substring(0,5):start;
            end = end.length()>8?end.substring(end.length()-8):end;
            return start+end;
        } catch (Exception e) {
            LogUtil.LOGE("error",e.toString());
        }

        return Calendar.getInstance().getTimeInMillis() + "";
    }



}

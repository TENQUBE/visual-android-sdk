package tenqube.parser.core;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.provider.Settings;
import android.provider.Telephony;
import android.text.TextUtils;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tenqube.parser.constants.Constants;
import tenqube.parser.model.RegData;
import tenqube.parser.model.Transaction;

import static tenqube.parser.core.ParserService.TAG;
import static tenqube.parser.core.ParserService.mIsDebug;
import static tenqube.parser.util.LogUtil.LOGI;
import static tenqube.parser.util.Validator.isDate;

class Utils {

    private static final Pattern spentMoneyPattern = Pattern.compile("\\s*([0-9,])+만원\\s*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE);
    private static final Pattern titlePattern = Pattern.compile("(.+)\\([0-9]+\\)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE);


    public static String filterTitle(@NotNull String title) {
//        "\u2068천은송\u2069(2)"

        Matcher matcher = titlePattern.matcher(title);
        if (matcher.matches()) {
            title = getMatcher(1, matcher);
        }

        title = title.replace("\u2069", "");
        title = title.replace("\u2068", "");
        title = title.replace("\u200E", "");
        title = title.replace("\u200B", "");

        return title.trim();

    }

    public static boolean isDefaultSMS(Context context, String pkgName) {
        String defaultSMS;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            defaultSMS = Telephony.Sms.getDefaultSmsPackage(context);
        } else {
            String defApp = Settings.Secure.getString(context.getContentResolver(), "sms_default_application");
            PackageManager pm = context.getApplicationContext().getPackageManager();
            Intent iIntent = pm.getLaunchIntentForPackage(defApp);
            ResolveInfo mInfo = pm.resolveActivity(iIntent,0);
            defaultSMS = mInfo.activityInfo.packageName;
        }

        return defaultSMS.equals(pkgName);
    }

    public static double transformSpentMoney(String spentMoney) {

        Matcher matcher = spentMoneyPattern.matcher(spentMoney);
        if (matcher.matches()) {

            String spentMoneyStr = getMatcher(1, matcher);
            if(TextUtils.isEmpty(spentMoneyStr))
                return 0;

            return Double.parseDouble(spentMoneyStr.replace(",","").replace(" ","").replace("O","0").trim())*10000;
        }

        return 0;

    }

    private static final Pattern[] KEYWORD_FILTERS = {

            Pattern.compile("\\s*누계\\s*[0-9,-]+[만원]*\\s*(.*)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE),
            Pattern.compile("\\s*누적\\s*[0-9,-]+원*\\s*(.*)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE),
            Pattern.compile("\\s*누적\\s*[0-9,-]+원*\\s*(.*)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE),
            Pattern.compile("\\s*잔액\\s*[0-9,-]+원*\\s*(.*)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE),
            Pattern.compile("\\s*통장잔액\\s*[0-9,-]+원*\\s*(.*)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE),
            Pattern.compile("\\s*지급가능액\\s*[0-9,-]+원*\\s*(.*)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE),
            Pattern.compile("\\s*잔여\\s*[0-9,-]*원+\\s*(.*)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE),
            Pattern.compile("\\s*가능액\\s*[0-9,-]*원+\\s*(.*)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE),
            Pattern.compile("\\s*적립예정\\s*[0-9,-]+원*\\s*(.*)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE),
            Pattern.compile("\\s*잔여한도\\s*[0-9,-]+원*\\s*(.*)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE),

            Pattern.compile("\\s*(.*)\\s+누계.*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE),
            Pattern.compile("\\s*(.*)\\s+누적.*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE),
            Pattern.compile("\\s*(.*)\\s+잔액.*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE),
            Pattern.compile("\\s*(.*)\\s+통장잔액.*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE),
            Pattern.compile("\\s*(.*)\\s+지급가능액.*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE),
            Pattern.compile("\\s*(.*)\\s+잔여.*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE),
            Pattern.compile("\\s*(.*)\\s+가능액.*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE),
            Pattern.compile("\\s*(.*)\\s+적립예정.*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE),
            Pattern.compile("\\s*(.*)\\s+잔여한도.*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE),
    };


    static String transformKeyword(String keyword){

        try {

            Matcher matcher;
            keyword = keyword.trim();
            keyword = keyword.replace("\t","");
            keyword = keyword.replace("<", "");
            keyword = keyword.replace(">", "");
            keyword = keyword.replaceAll("\\\\", "");
            keyword = keyword.replaceAll("  ", "");
            keyword = keyword.replace("\n", "");
            int length = keyword.length();
            if (length > 0) {
                length = length - 1;
                if (keyword.substring(0, 1).equals("(") && ")".equals(keyword.substring(length))) {
                    keyword = keyword.substring(1, length);
                } else if (keyword.substring(length).equals(".") || keyword.substring(length).equals("(") || keyword.substring(length).equals(",")) {
                    keyword = keyword.substring(0, length);
                }
            }

            if (
                    keyword.contains("잔액") ||
                    keyword.contains("누계") ||
                    keyword.contains("누적") ||
                    keyword.contains("가능액") ||
                    keyword.contains("잔여") ||
                    keyword.contains("적립예정")

                    ) {

                for (Pattern pattern : KEYWORD_FILTERS) {

                    matcher = pattern.matcher(keyword);
                    if (matcher.matches()) {
                        return "".equals(getMatcher(1, matcher))?Constants.KEYWORD_EMPTY : getMatcher(1, matcher);
                    }
                }
            }
        }catch (Exception e) {
            return keyword;
        }

        return keyword;
    }



    static boolean matchingCardName(String duplCard,String parsedCard, String duplIsBank, String parsedIsBank){

        if(!parsedIsBank.equals(duplIsBank)){
            return true;
        }
        if(duplCard == null)duplCard = Constants.NONE;
        if(parsedCard == null)parsedCard = Constants.NONE;

        duplCard = duplCard.toLowerCase();
        parsedCard = parsedCard.toLowerCase();
        duplCard = transformCardName(duplCard);
        parsedCard = transformCardName(parsedCard);


        return duplCard.equals(parsedCard)   ||
                duplCard.contains(parsedCard)||
                parsedCard.contains(duplCard);
    }


    static String transformCardName(String cardName){

        if(cardName == null)cardName = Constants.NONE;

        cardName = cardName.replace("체크","");

        cardName = cardName.replace("가족","");

        cardName = cardName.replace("법인","");

        cardName = cardName.replace("은행","");

        cardName = cardName.replace("신용","");

        cardName = cardName.replace("농협","NH");

        cardName = cardName.replace("외환","하나");

        cardName = cardName.replace("KEB","하나");

        cardName = cardName.replace("citi","씨티");

        cardName = cardName.replace("국민","KB");

        cardName = cardName.replace("부산","BNK");

        cardName = cardName.replace("기업","IBK");

        cardName = cardName.replace("MG","새마을");

        cardName = cardName.replace("스탠차타드","SC");
        cardName = cardName.replace("전북","JB");


        return  cardName;
    }



    static String transformFullSMS(String fullSMS){

        if(fullSMS == null)fullSMS = "None";
        fullSMS = fullSMS.replace("[FW]", "");
        fullSMS = fullSMS.replace("　", " ");
        fullSMS = fullSMS.replace("↵"," ");
        fullSMS = fullSMS.replace("FW>", "");
        fullSMS = fullSMS.replace("듀>","");
        fullSMS = fullSMS.replace("[재전송]","");
        fullSMS = fullSMS.replace("[투]","");
        fullSMS = fullSMS.replace("[Web발신]\n", "");
        fullSMS = fullSMS.replace("[Web발신]", "");
        fullSMS = fullSMS.replace("[Web 발신]", "");
        fullSMS = fullSMS.replace("[web 발신]", "");
        fullSMS = fullSMS.replace("[web발신]", "");
        fullSMS = fullSMS.replace("[WEB발신]", "");
        fullSMS = fullSMS.replace("[WEB 발신]", "");
        fullSMS = fullSMS.replace("(재전송)", "");
        fullSMS = fullSMS.replace("[", " ");
        fullSMS = fullSMS.replace("]", " ");
        if(fullSMS.length()>4&&
                fullSMS.substring(0,4).contains("FW")){
            fullSMS = fullSMS.replace("FW", "");
        }

        return fullSMS.replace("\n", " ");
    }


    static String transformCurrency(String currency){

        if(TextUtils.isEmpty(currency) || "null".equalsIgnoreCase(currency))
            return "";

        if ("엔".equals(currency.trim())) {
            return "JPY";
        } else if ("위안".equals(currency.trim())) {
            return  "CNY";
        } else if ("유로".equals(currency.trim())) {
            return  "EUR";
        } else if ("달러". equals(currency.trim())) {
            return "USD";
        } else {
            return  currency;
        }

    }

    static String getCardNumUntilFour(String cardNum){

        if(TextUtils.isEmpty(cardNum))
            return "";

        cardNum = cardNum.replace("(","");
        cardNum = cardNum.replace(")","");
        cardNum = cardNum.replace("-","");
        cardNum = cardNum.replace("X","*");
        cardNum = cardNum.replace("#","*");
        cardNum = cardNum.replaceAll(" ","");
        return  cardNum.length()>4?
                "("+cardNum.substring(cardNum.length()-4)+")"
                :
                "("+cardNum+")";
    }

    private static final Pattern[] pDate = {

            Pattern.compile("\\s*(\\d{1,2})/(\\d{1,2})\\s*,*\\s*(\\d{1,2}):(\\d{1,2})\\s*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE),
            Pattern.compile("\\s*(\\d{1,2})\\.(\\d{1,2})\\s*,*\\s*(\\d{1,2}):(\\d{1,2})\\s*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE),
            Pattern.compile("\\s*(\\d{1,2})월(\\d{1,2})일\\s*(\\d{1,2})시(\\d{1,2})분\\s*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE),

            Pattern.compile("\\s*(\\d{1,2})월(\\d{1,2})일\\s*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE),
            Pattern.compile("\\s*(\\d{1,2})/(\\d{1,2})\\s*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE),

            Pattern.compile("\\s*(\\d{1,2})/(\\d{1,2})\\s*\\s*(\\d{1,2}):(\\d{1,2}):(\\d{1,2})\\s*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE),


            Pattern.compile("\\s*(\\d{4})\\.(\\d{1,2})\\.(\\d{1,2})\\s*(\\d{1,2}):(\\d{1,2}):(\\d{1,2})\\s*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE),
            Pattern.compile("\\s*(\\d{4})/(\\d{1,2})/(\\d{1,2})\\s*(\\d{1,2}):(\\d{1,2})\\s*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE),
            Pattern.compile("\\s*(\\d{4})\\s*-\\s*(\\d{1,2})\\s*-\\s*(\\d{1,2})\\s*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE),
            Pattern.compile("\\s*(\\d{4})년\\s*(\\d{1,2})월\\s*(\\d{1,2})일\\s*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE),

            Pattern.compile("\\s*(\\d{1,2}):(\\d{1,2}):(\\d{1,2})\\s*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE),



    };

    static String transformDate(Calendar smsCal, String spentDate) {

        String year = smsCal.get(Calendar.YEAR)+"";
        String second = DateUtil.decimalFormat.format(smsCal.get(Calendar.SECOND))+"";
        String month = DateUtil.decimalFormat.format((smsCal.get(Calendar.MONTH) + 1));
        String day = DateUtil.decimalFormat.format(smsCal.get(Calendar.DATE))+"";
        String hour = DateUtil.decimalFormat.format(smsCal.get(Calendar.HOUR_OF_DAY))+"";
        String minute = DateUtil.decimalFormat.format(smsCal.get(Calendar.MINUTE))+"";

        try {

            if(!TextUtils.isEmpty(spentDate)) {


                for (int i = 0; i < pDate.length; i++) {
                    Matcher matcher = pDate[i].matcher(spentDate);

                    if (matcher.matches()) {

                        switch (i) {

                            case 0:
                            case 1:
                            case 2:
                                month = String.format(Locale.KOREA, "%02d", Integer.parseInt(getMatcher(1, matcher)));
                                day = String.format(Locale.KOREA,"%02d",  Integer.parseInt(getMatcher(2, matcher)));
                                hour = "24".equals(getMatcher(3, matcher)) ? "00" : String.format(Locale.KOREA,"%02d",  Integer.parseInt(getMatcher(3, matcher)));
                                minute = String.format(Locale.KOREA,"%02d",  Integer.parseInt(getMatcher(4, matcher)));
                                break;
                            case 3:
                            case 4:
                                month = String.format(Locale.KOREA, "%02d", Integer.parseInt(getMatcher(1, matcher)));
                                day = String.format(Locale.KOREA,"%02d",  Integer.parseInt(getMatcher(2, matcher)));
                                break;
                            case 5:
                                month = String.format(Locale.KOREA, "%02d", Integer.parseInt(getMatcher(1, matcher)));
                                day = String.format(Locale.KOREA,"%02d",  Integer.parseInt(getMatcher(2, matcher)));
                                hour = "24".equals(getMatcher(3, matcher)) ? "00" : String.format(Locale.KOREA,"%02d",  Integer.parseInt(getMatcher(3, matcher)));
                                minute = String.format(Locale.KOREA,"%02d",  Integer.parseInt(getMatcher(4, matcher)));
                                second = String.format(Locale.KOREA,"%02d",  Integer.parseInt(getMatcher(5, matcher)));
                                break;

                            case 6:
                                year = String.format(Locale.KOREA, "%02d", Integer.parseInt(getMatcher(1, matcher)));
                                month = String.format(Locale.KOREA, "%02d", Integer.parseInt(getMatcher(2, matcher)));
                                day = String.format(Locale.KOREA,"%02d",  Integer.parseInt(getMatcher(3, matcher)));
                                hour = "24".equals(getMatcher(4, matcher)) ? "00" : String.format(Locale.KOREA,"%02d",  Integer.parseInt(getMatcher(4, matcher)));
                                minute = String.format(Locale.KOREA,"%02d",  Integer.parseInt(getMatcher(5, matcher)));
                                second = String.format(Locale.KOREA,"%02d",  Integer.parseInt(getMatcher(6, matcher)));
                                break;
                            case 7:
                                year = String.format(Locale.KOREA, "%02d", Integer.parseInt(getMatcher(1, matcher)));
                                month = String.format(Locale.KOREA, "%02d", Integer.parseInt(getMatcher(2, matcher)));
                                day = String.format(Locale.KOREA,"%02d",  Integer.parseInt(getMatcher(3, matcher)));
                                hour = "24".equals(getMatcher(4, matcher)) ? "00" : String.format(Locale.KOREA,"%02d",  Integer.parseInt(getMatcher(4, matcher)));
                                minute = String.format(Locale.KOREA,"%02d",  Integer.parseInt(getMatcher(5, matcher)));
                                break;
                            case 8:
                            case 9:
                                year = String.format(Locale.KOREA, "%02d", Integer.parseInt(getMatcher(1, matcher)));
                                month = String.format(Locale.KOREA, "%02d", Integer.parseInt(getMatcher(2, matcher)));
                                day = String.format(Locale.KOREA,"%02d",  Integer.parseInt(getMatcher(3, matcher)));
                                break;
                            case 10:
                                hour = "24".equals(getMatcher(1, matcher)) ? "00" : String.format(Locale.KOREA,"%02d",  Integer.parseInt(getMatcher(1, matcher)));
                                minute = String.format(Locale.KOREA,"%02d",  Integer.parseInt(getMatcher(2, matcher)));
                                second = String.format(Locale.KOREA,"%02d",  Integer.parseInt(getMatcher(3, matcher)));
                                break;
                        }

                    }
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        String resultDate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;

        return isDate(resultDate)? resultDate : null;
    }

    static String transformSpentMoney(double value) {

        if (value == 0) {
            return "0";
        }
        String pattern = "###,###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        return decimalFormat.format(value);

    }

    static String transformSender(String sender){


        if(!TextUtils.isEmpty(sender)){

            sender = sender.replace("+822", "");
            sender = sender.replace("+82", "");

            if(sender.length()>2 &&
                sender.substring(0,3).equals("822")){
                sender = sender.replaceFirst("822", "");
            }


            if(sender.length()>1 &&
                sender.substring(0,2).equals("82")){
                sender = sender.replaceFirst("82", "");
            }

            sender = sender.replace("-","");
            sender = sender.replace("+","");
            sender = sender.replace("#","");

            if(sender.length()>1 &&
                sender.substring(0,2).equals("02")) {
                sender = sender.replaceFirst("02", "");
            }

            if(sender.length()>1 &&
                sender.substring(0,1).equals("0")){
                //  첫번째가 0일때
                sender = sender.replaceFirst("0", "");
            }

            return sender;
        }

        return "";
    }


    static String transformRepSender(String fullSMS){

        if(!TextUtils.isEmpty(fullSMS)) {

            if(fullSMS.contains("하이투자")) {
                return "하이투자";
            } else if(fullSMS.contains("SK증권")) {
                return "SK증권";
            } else if(fullSMS.contains("신영증권")) {
                return "신영증권";
            } else if(fullSMS.contains("동부증권")) {
                return "동부증권";
            } else if(fullSMS.contains("유진투자")) {
                return "유진투자";
            }
        }

        return null;
    }


    static String getMatcher(int pos, Matcher matcher) {

        try {
            return matcher.group(pos);
        } catch (IndexOutOfBoundsException e) {
            return "";
        }
    }



    static class RegComparator implements Comparator<RegData>
    {
        public int compare(RegData reg1, RegData reg2)
        {
            return reg2.priority == reg1.priority ?
                    reg1.userPriority < reg2.userPriority ? 1:-1
                    : reg1.priority < reg2.priority ? 1 : -1;
        }
    }

//    public static class SpentDateComparator implements Comparator<Transaction>
//    {
//        public int compare(Transaction tran1, Transaction tran2)
//        {
//            return DateUtil.convertStringToCalendarFULL(tran1.spentDate).before(DateUtil.convertStringToCalendarFULL(tran2.spentDate))? -1 : 1;
//        }
//    }

    static ArrayList<Transaction> distinctTransactions(ArrayList<Transaction> transactions) {

        LinkedHashMap<String, Transaction> map = new LinkedHashMap<>();
        for (Transaction transaction : transactions) {
            String key = transaction.identifier;
            map.put(key, transaction);
        }

        return new ArrayList<>(map.values());
    }


    static int getSMSType(int smsType) {
        return  Constants.SMSType.NOTIFICATION.ordinal() == smsType ?
                Constants.SMSType.NOTIFICATION.ordinal()
                :
                Constants.SMSType.SMS.ordinal();
    }

    static final Pattern numberPattern = Pattern.compile(".*[0-9]+.*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE);
    static boolean isSpendSMS(String fullSMS) {

        Matcher matcher = numberPattern.matcher(fullSMS);
        return matcher.matches();
    }

    static String makeCardKey(CardTableData parsedCard) {
        StringBuilder key = new StringBuilder();
        key.append(TextUtils.isEmpty(parsedCard.cardName)?"":parsedCard.cardName);
        key.append(TextUtils.isEmpty(parsedCard.cardNum)?"":parsedCard.cardNum);
        key.append(parsedCard.cardType);
        key.append(parsedCard.cardSubType);
        return key.toString();

    }

    static String makeSpentMoneyDwTypeKey(double spentMoney, int dwType) {

        StringBuilder key = new StringBuilder();
        key.append(spentMoney);
        key.append(";");
        key.append(dwType);
        return key.toString();
    }

    static String makeRegKey(String repSender, int smsType) {
        StringBuilder key = new StringBuilder();
        key.append(TextUtils.isEmpty(repSender)?"":repSender);
        key.append(smsType);
        return key.toString();
    }


    /**
     * ex )

     2018 01 9 12:22:22 (smsDate)

     문자 12/31

     smsDate의 년도, 년도-1 로 spentDate 생성

     spentDate ->   1)2018 01 09 12:22:22
     2)2016 12 31 12:22:22

     range1 = (smsDate, spentDate1)
     range2 = (smsDate, spentDate2)

     min(range1, range2) 의 spentDate로 결정
     */
    static String getCanceledSpentDate(Calendar smsCalendar, Calendar spentCalendar) {

        LOGI(TAG,"getCanceledSpentDate" + "smsCalendar" +  DateUtil.getStringDateAsYYYYMMddHHmmss(smsCalendar));

        Calendar spentCalendar2 = Calendar.getInstance();
        spentCalendar2.setTimeInMillis(spentCalendar.getTimeInMillis());//2018-12-31
        spentCalendar2.add(Calendar.YEAR, -1);//2017-12-31

        int diffDay = Math.abs(DateUtil.getDiffDateAsCalendar(smsCalendar, spentCalendar));//11 * 30
        int diffDay2 = Math.abs(DateUtil.getDiffDateAsCalendar(smsCalendar, spentCalendar2));// 9

        LOGI(TAG,"getCanceledSpentDate" + "diffDay" + diffDay);
        LOGI(TAG,"getCanceledSpentDate" + "diffDay" +  DateUtil.getStringDateAsYYYYMMddHHmmss(spentCalendar));

        LOGI(TAG,"getCanceledSpentDate" + "diffDay2" + diffDay2);
        LOGI(TAG,"getCanceledSpentDate" + "diffDay2" + DateUtil.getStringDateAsYYYYMMddHHmmss(spentCalendar2));
        if(diffDay <= diffDay2) {
            return DateUtil.getStringDateAsYYYYMMddHHmmss(spentCalendar);
        } else {
            return DateUtil.getStringDateAsYYYYMMddHHmmss(spentCalendar2);
        }

    }



}

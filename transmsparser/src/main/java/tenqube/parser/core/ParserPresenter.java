package tenqube.parser.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import tenqube.parser.constants.Constants;
import tenqube.parser.finsmsparser.presentation.FinSmsParser;
import tenqube.parser.finsmsparser.presentation.FinSmsParserBuilder;
import tenqube.parser.finsmsparser.presentation.model.FinanceResponse;
import tenqube.parser.finsmsparser.presentation.model.FinancialProduct;
import tenqube.parser.model.ParserResult;
import tenqube.parser.model.ParsingRule;
import tenqube.parser.model.RegData;
import tenqube.parser.model.ResultCode;
import tenqube.parser.model.SMS;
import tenqube.parser.model.Sender;
import tenqube.parser.model.Transaction;

import static tenqube.parser.constants.Constants.ALL;
import static tenqube.parser.constants.Constants.LG_PAY;
import static tenqube.parser.constants.Constants.SAMSUNG_PAY;
import static tenqube.parser.core.BaseQueryHelper.IN;
import static tenqube.parser.core.ParserReaderContract.TransactionsTable.INSERT_TRANSACTION;
import static tenqube.parser.core.ParserService.mIsDebug;
import static tenqube.parser.core.Utils.getSMSType;
import static tenqube.parser.core.Utils.isSpendSMS;
import static tenqube.parser.core.Utils.makeCardKey;
import static tenqube.parser.core.Utils.makeRegKey;
import static tenqube.parser.core.Utils.makeSpentMoneyDwTypeKey;
import static tenqube.parser.core.Utils.transformRepSender;
import static tenqube.parser.core.Utils.transformSender;
import static tenqube.parser.util.LogUtil.LOGI;
import static tenqube.parser.util.LogUtil.makeLogTag;


class ParserPresenter {

    private static final String TAG = makeLogTag(ParserPresenter.class);

    private Context mContext;
    private ParserMapper mMapper;
    private RegHandler mRegHandler;
    private ParserDao mParserDao;
    private Map<String, ArrayList<RegData>> mRegMap = new HashMap<>();
    private Map<String, String> mSendersMap =  new HashMap<>();
    private Map<String, ArrayList<TransactionTableData>> mDuplMap =  new HashMap<>();
    private Map<String, ArrayList<TransactionTableData>> mMovingAssetMap =  new HashMap<>();
    private Map<String, ArrayList<TransactionTableData>> mOffsetMap =  new HashMap<>();

    private Map<String, TransactionTableData> mTransactionMap =  new HashMap<>();
    private Map<String, CardTableData> mCardMap =  new HashMap<>();
    private FinSmsParser finSmsParser;

    ParserPresenter(Context context)  {
        this.mContext = context;
        mParserDao = new ParserDao(context);
        mMapper = new ParserMapper();
        mRegHandler = new RegHandler(context);
        finSmsParser = new FinSmsParserBuilder().with(context).build();
        LOGI(TAG, "ParsingHelper constructor");
    }

    void initTransactions() {

        mParserDao.initTransactions();
    }

    /**************************************      BULK         *************************************************/


    void setSendersWithBulk(){
        LOGI(TAG, "setSenders");
        mSendersMap = mParserDao.getSendersMap();
    }

    /**
     * is_success = 0 인 리스트 함께 보냄
     * 0. sender 필터
     * 1. 정규표현식을 통한 데이터 추출
     * 2. 중복 데이터 확인
     * 3. 중복 x 상쇄 Tran 확인
     * 4. 중복 x 상쇄 x 자산이동 Tran 확인
     * 5. 잔액 변경

     * @param sms 수신된 문자
     * @return 파싱된 Tran List
     */
    ArrayList<Transaction> parseWithBulk(SMS sms) {

        TransactionTableData transaction;
        TransactionTableData offsetTran = null;
        TransactionTableData movingAssetTran = null;

        if(!isSpendSMS(sms.getFullSms()))
            return null;
        //0. sender 필터
        String repSender = getRepSender(sms);

        LOGI(TAG, "0. sender 대표 필터" + repSender);
        if (repSender == null)
            return null;

        if(isExistTranWithBulk(sms.getSmsId(), sms.getSmsType())) {
            LOGI(TAG, "isExistTranWithBulk: 이미 존재");
            return null;
        }

        //1. 정규표현식을 통한 데이터 확인
        transaction = getBulkTransactionWithReg(sms, repSender);
        if (transaction == null) {
            LOGI(TAG, "1. 정규표현식을 통한 데이터 확인: FALSE");
            LOGI(TAG, "NOT PARSED: "+sms.toString());
            return null;
        }

        LOGI(TAG, "1. 정규표현식을 통한 데이터 확인: TRUE" + transaction.toString());
        //2. 중복 데이터 확인
        LOGI(TAG, "2. 중복 데이터 확인");
        TransactionTableData duplicationTran = getDuplicationTranWithBulk(transaction);

        //중복
        if (duplicationTran != null) {

            LOGI(TAG, "2. 중복 데이터 확인: TRUE" + duplicationTran.toString());
            //중복인 경우 우선순위에 따라 정보 변경
            transaction = mRegHandler.combineTran(duplicationTran, transaction);

            //변경된 중복데이터 맵에 추가
            String key = makeSpentMoneyDwTypeKey(transaction.spentMoney, transaction.dwType);
            if(mDuplMap!=null) {
                ArrayList<TransactionTableData> duplTrans = mDuplMap.get(key);
                duplTrans.add(transaction);
                mDuplMap.put(key, duplTrans);
            }

        } else {
            //중복 아님
            LOGI(TAG, "2. 중복 데이터 확인: FALSE");

            //상쇄 확인
            offsetTran = getOffsetTranWithBulk(transaction);
            LOGI(TAG, "3. 중복 x 상쇄 Tran 확인");

            //상쇄
            if (offsetTran != null) {

                LOGI(TAG, "3. 중복 x -> 상쇄 Tran 확인: TRUE" + offsetTran.toString());
                transaction = mRegHandler.combineTranWithOffset(offsetTran, transaction);


            } else {
                //상쇄아님
                LOGI(TAG, "3. 중복 x -> 상쇄 Tran 확인: FALSE");

                //자산이동 확인
                LOGI(TAG, "4. 중복 x -> 상쇄 x -> 자산이동 Tran 확인");

                movingAssetTran = getMovingAssetTranWithBulk(transaction);

                //자산이동
                if (movingAssetTran != null) {
                    LOGI(TAG, "4. 중복 x 상쇄 x 자산이동 Tran 확인: TRUE" + movingAssetTran.toString());

                    transaction = mRegHandler.combineTranWithMovingAsset(transaction);
                    movingAssetTran = mRegHandler.combineTranWithMovingAsset(movingAssetTran);

                } else {

                    LOGI(TAG, "4. 중복 x 상쇄 x 자산이동 Tran 확인: FALSE");

                }
            }
        }
        //잔액 업데이트
        if(transaction != null) {
            transaction = mRegHandler.updateBalance(transaction);
            transaction.isCurrentTran = true;
        }

        //목록 넣기
        ArrayList<Transaction> resultTrans = getResultTransactionsWithBulk(transaction, offsetTran, movingAssetTran);

        if(resultTrans.size() == 0)
            return null;

        LOGI(TAG, "최종 리턴 사이즈:" + resultTrans.size());
        return resultTrans;
    }

    public String getRepSender(SMS sms) {
        String repSender;

        repSender = getRepSenderWithBulk(transformSender(sms.getSender()));

        if(repSender == null) {
            repSender = getRepSenderWithBulk(sms.getSender());
        }

        if(repSender == null) {
            repSender = getRepSenderWithBulk(transformSender(sms.getDisplaySender()));
        }

        if(repSender == null) {
            repSender = getRepSenderWithBulk(sms.getDisplaySender());
        }

        if(repSender == null) {
            repSender = transformRepSender(sms.getFullSms());
        }

        return repSender;

    }

    private ArrayList<Transaction> getResultTransactionsWithBulk(TransactionTableData parsedTransaction,
                                                                 TransactionTableData offsetTran,
                                                                 TransactionTableData movingAssetTran) {

        LOGI(TAG, "getResultTransactionsWithBulk");

        ArrayList<Transaction> resultTrans = new ArrayList<>();

        try {
            ArrayList<TransactionTableData> transactions = new ArrayList<>();
            if(parsedTransaction != null) transactions.add(parsedTransaction);
            if(offsetTran != null) transactions.add(offsetTran);
            if(movingAssetTran != null) transactions.add(movingAssetTran);

            //리턴 맵핑
            for (TransactionTableData tran : transactions) {
                if(mTransactionMap != null)
                    mTransactionMap.put(tran.identifier, tran);

                if(mCardMap!=null)
                    mCardMap.put(makeCardKey(tran.cardTableData), tran.cardTableData);//최종 잔액 업데이트

                Transaction resultTran = mMapper.transform(tran);
                if(resultTran != null) {
                    resultTrans.add(resultTran);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultTrans;
    }

    private String getRepSenderWithBulk(String sender) {

        if(TextUtils.isEmpty(sender))
            return null;

        LOGI(TAG, "getRepSender");
        return mSendersMap==null?
                mParserDao.getRepSender(sender)
                :
                mSendersMap.get(sender);
    }



    /**
     * 정규표현식을 통한 Transaction 추출
     *
     * 1. getRegDatas map.get(sender) == null? where sender and smsType
     * 2. matching start
     * 3. matched -> TransactionTableData , cardTableData, 데이터 셋팅
     *
     * @param sms 수신된 문자 or 알림
     * @return 파싱된 Transaction
     */
    private TransactionTableData getBulkTransactionWithReg(SMS sms, String repSender){
        LOGI(TAG, "getBulkTransactionWithReg start" + sms.toString());

        TransactionTableData parsedTran;

        String newFullSms  = Utils.transformFullSMS(sms.getFullSms());
        LOGI(TAG, "getBulkTransactionWithReg newFullSms" + newFullSms);

        parsedTran = mRegHandler.getTransactionWithReg(getRegDatasWithBulk(repSender, sms.getSmsType()), sms);
        if(parsedTran != null) {
            parsedTran.cardTableData = getCardWithBulk(parsedTran.cardTableData);
            if(parsedTran.userPriority < 10){
                updateUserPriorityWithBulk(repSender, sms.getSmsType());
            }
        }

        return  parsedTran;

    }

    private ArrayList<RegData> getRegDatasWithBulk(String repSender, int smsType) {
        smsType = getSMSType(smsType);
        String key = makeRegKey(repSender, smsType);
        if (mRegMap == null) mRegMap = new HashMap<>();
        ArrayList<RegData> regDatas = mRegMap.get(key);
        if(regDatas == null) {
            regDatas = mParserDao.getRegDatasWithBulk(repSender, smsType);
        }
        return regDatas;
    }

    /**
     * 카드 정보 구하기
     *
     * 카드 정보 없는 경우 인서트
     * 카드 정보 있는 경우 추출
     *
     * @param parsedCard 파싱된 카드 정보
     * @return 카드 정보
     */
    private CardTableData getCardWithBulk(CardTableData parsedCard) throws SQLException {

        LOGI(TAG, "getCard");

        if(mCardMap == null) mCardMap = new HashMap<>();

        String key = makeCardKey(parsedCard);
        CardTableData cardTableData = mCardMap.get(key);
        if(cardTableData != null) {

            return cardTableData;

        } else {

            cardTableData = mParserDao.getCard(parsedCard);
            mCardMap.put(key, cardTableData);
        }


        return cardTableData;

    }


    private boolean isExistTranWithBulk(int smsId, int smsType) {
        if (smsId == 0)
            return false;

        if(smsType == Constants.SMSType.SMS.ordinal()) {
            if (PrefUtils.getInstance(mContext).loadIntValue(PrefUtils.MAX_SMS_ID, 0) >= smsId) {
                LOGI(TAG,"maxSmsId:" + PrefUtils.getInstance(mContext).loadIntValue(PrefUtils.MAX_SMS_ID, 0));
                LOGI(TAG,"smsId:" + smsId);

                return true;
            }
        } else if(smsType == Constants.SMSType.MMS.ordinal()) {
            if (PrefUtils.getInstance(mContext).loadIntValue(PrefUtils.MAX_MMS_ID, 0) >= smsId) {

                LOGI(TAG,"maxMmsId:" + PrefUtils.getInstance(mContext).loadIntValue(PrefUtils.MAX_MMS_ID, 0));
                LOGI(TAG,"smsId:" + smsId);

                return true;
            }
        }

        return false;

    }

    /**
     * 중복 Transaction 추출 함수
     *
     * @param tran 파싱된 Transaction
     * @return 중복된 Transaction
     */
    private TransactionTableData getDuplicationTranWithBulk(TransactionTableData tran) throws SQLException {
        LOGI(TAG, "getDuplicationTran");

        TransactionTableData duplicationTran = null;
        try {
            //key spendMoney, dwType
            String key = makeSpentMoneyDwTypeKey(tran.spentMoney, tran.dwType);
            LOGI(TAG, "getDuplicationTran key" + key);
            if(mDuplMap == null) mDuplMap = new HashMap<>();

            ArrayList<TransactionTableData> duplTrans = mDuplMap.get(key);
            duplicationTran = getDuplicationTranWithBulk(key, duplTrans, tran, true);

            if(duplicationTran == null) {
                int dwType = tran.dwType == Constants.DWType.DEPOSIT.ordinal()? Constants.DWType.WITHDRAW.ordinal() : Constants.DWType.DEPOSIT.ordinal();
                key =  makeSpentMoneyDwTypeKey(-1 * tran.spentMoney, dwType);
                duplTrans = mDuplMap.get(key);
                duplicationTran = getDuplicationTranWithBulk(key, duplTrans, tran, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return duplicationTran;


    }

    private TransactionTableData getDuplicationTranWithBulk(String key, ArrayList<TransactionTableData> duplTrans, TransactionTableData tran, boolean isInsert){
        LOGI(TAG, "getDuplicationTranWithBulk");

        TransactionTableData duplicationTran = null;

        try {
            if(duplTrans == null || duplTrans.isEmpty()) {
                if(isInsert) {
                    duplTrans = new ArrayList<>();
                    duplTrans.add(tran);
                    if(mDuplMap!=null)
                        mDuplMap.put(key, duplTrans);
                }
                return  null;
            } else {
                //존재
                Calendar spendCal = DateUtil.convertStringToCalendarFULL(tran.spentDate);
                Calendar fixedCal = DateUtil.convertStringToCalendarFULL(tran.spentDate);
                fixedCal.add(Calendar.HOUR_OF_DAY, -14);
                spendCal.add(Calendar.MINUTE, 5);

                long endSpendTime = spendCal.getTimeInMillis();
                long fixedSpendTime =  fixedCal.getTimeInMillis();

                if (SAMSUNG_PAY.equals(tran.cardTableData.cardName) || LG_PAY.equals(tran.cardTableData.cardName)) {
                    spendCal.add(Calendar.HOUR_OF_DAY, -13);
                } else {
                    spendCal.add(Calendar.MINUTE, -15);
                }

                long startSpendTime = spendCal.getTimeInMillis();

                for(int i = duplTrans.size() - 1 ; i >= 0; i--) {
                    TransactionTableData dupl = duplTrans.get(i);

                    long duplSpendTime = DateUtil.convertStringToCalendarFULL(dupl.spentDate).getTimeInMillis();

                    if(duplSpendTime >= fixedSpendTime) {
                        if((startSpendTime < duplSpendTime && endSpendTime >= duplSpendTime) &&
                                mRegHandler.isDuplicate(dupl.originInfos, tran.originInfos)) {
                            duplicationTran = dupl;
                            duplTrans.remove(i);
                            break;
                        }
                    }
                }

                //잡히지 않은경우 추가시켜줌
                if(isInsert && duplicationTran == null)
                    duplTrans.add(tran);

//                Collections.sort(duplTrans, new Utils.TranComparator());
                if(mDuplMap!=null)
                    mDuplMap.put(key, duplTrans);


            }
        } catch (Exception e){
            e.printStackTrace();
        }




        return duplicationTran;

    }

    private TransactionTableData getOffsetTranWithBulk(TransactionTableData parsedTran) throws SQLException {
        LOGI(TAG, "getOffsetTran");

        try {
            if (parsedTran.spentMoney < 0) {

                String key = makeSpentMoneyDwTypeKey(-1*parsedTran.spentMoney ,parsedTran.dwType);
                ArrayList<TransactionTableData> offsetTrans = mOffsetMap.get(key);

                if(offsetTrans == null || offsetTrans.isEmpty())
                    return null;

                TransactionTableData offsetTran = null;
                Calendar spendCal = DateUtil.convertStringToCalendarFULL(parsedTran.spentDate);
                spendCal.add(Calendar.DATE, 1);

                long endSpendTime = spendCal.getTimeInMillis();

                spendCal.add(Calendar.MONTH, -2);

                long startSpendTime = spendCal.getTimeInMillis();

                for(int i = offsetTrans.size() - 1 ; i >= 0; i--) {
                    TransactionTableData offset = offsetTrans.get(i);
                    long offsetSpendTime = DateUtil.convertStringToCalendarFULL(offset.spentDate).getTimeInMillis();
                    if(
                            (startSpendTime <= offsetSpendTime && endSpendTime > offsetSpendTime) &&
                                    (parsedTran.keyword.equals(offset.keyword) ||
                                    offset.cardTableData.cardName.contains(parsedTran.cardTableData.cardName) ||
                                    offset.cardTableData.cardName.contains(Utils.transformCardName(parsedTran.cardTableData.cardName))
                                    )
                            ) {
                        offsetTran = offset;
                        offsetTran.isOffset = Constants.YES;
                        offsetTrans.remove(i);
                        break;
                    }

                }
//                Collections.sort(offsetTrans, new Utils.TranComparator());
                if(mOffsetMap != null)
                    mOffsetMap.put(key, offsetTrans);
                return offsetTran;


            } else {
                if(mOffsetMap == null)mOffsetMap = new HashMap();
                String key = makeSpentMoneyDwTypeKey(parsedTran.spentMoney, parsedTran.dwType);
                ArrayList<TransactionTableData> offsetTrans = mOffsetMap.get(key);
                if(offsetTrans == null)offsetTrans = new ArrayList<>();
                offsetTrans.add(parsedTran);
                mOffsetMap.put(key, offsetTrans);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    /**
     * 자산이동 Transaction 추출 함수
     * @param tran 파싱된 Transaction
     * @return 자산이동 Transaction
     */
    private TransactionTableData getMovingAssetTranWithBulk(@NotNull TransactionTableData tran) throws SQLException {

        LOGI(TAG, "getMovingAssetTran");

        try {
            if (tran.cardTableData.cardType == Constants.CardType.BANK_ACCOUNT.ordinal()) {
                String key = tran.spentMoney + "";
                if(mMovingAssetMap == null)  mMovingAssetMap = new HashMap<>();

                ArrayList<TransactionTableData> assetTrans = mMovingAssetMap.get(key);

                if(assetTrans == null || assetTrans.isEmpty()) {
                    assetTrans = new ArrayList<>();
                    assetTrans.add(tran);

                    mMovingAssetMap.put(key, assetTrans);
                    return  null;
                } else {

                    TransactionTableData movingAssetTran = null;
                    //존재
                    Calendar spendCal = DateUtil.convertStringToCalendarFULL(tran.spentDate);
                    spendCal.add(Calendar.MINUTE, 3);
                    long endSpendTime = spendCal.getTimeInMillis();

                    spendCal.add(Calendar.MINUTE, -6);
                    long startSpendTime = spendCal.getTimeInMillis();

                    for(int i = assetTrans.size() - 1 ; i >= 0; i--) {
                        TransactionTableData asset = assetTrans.get(i);
                        long assetSpendTime = DateUtil.convertStringToCalendarFULL(asset.spentDate).getTimeInMillis();

                        if((startSpendTime < assetSpendTime && endSpendTime >= assetSpendTime) &&
                                !(asset.cardTableData.cardName.equals(tran.cardTableData.cardName) &&
                                        asset.cardTableData.cardNum.equals(tran.cardTableData.cardNum))&&
                                asset.dwType != tran.dwType) {
                            movingAssetTran = asset;
                            assetTrans.remove(i);
                            break;
                        }

                    }

                    //안잡힌경우 데이터 추가
                    if(movingAssetTran == null)
                        assetTrans.add(tran);
//                    Collections.sort(assetTrans, new Utils.TranComparator());
                    mMovingAssetMap.put(key, assetTrans);
                    return movingAssetTran;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    void initMap() {

        mRegMap =  new HashMap<>();
        mSendersMap =  new HashMap<>();
        mDuplMap =  new HashMap<>();
        mMovingAssetMap =  new HashMap<>();
        mOffsetMap =  new HashMap<>();
        mTransactionMap =  new HashMap<>();
        mCardMap =  new HashMap<>();

    }

    void initFinancialProductRules() {
        finSmsParser.cacheAllFinanceRules();
    }

    void insertTransactionAndMaxSmsId(ArrayList<Transaction> transactions) {
        LOGI(TAG, "insertTransactionAndMaxSmsId");

        int maxSMSId = 0;
        int maxMMSId = 0;

        try {

            Map<Integer, RegData> regDataMap = new HashMap<>();
            Map<Integer, CardTableData> cardMap = new HashMap<>();

            ArrayList<String> values = new ArrayList<>();
            for(int i = 0; i < transactions.size() ; i++ ) {

                Transaction tran = transactions.get(i);
                if(mTransactionMap!=null) {
                    TransactionTableData transactionTableData = mTransactionMap.get(tran.identifier);

                    if(transactionTableData!=null) {
                        transactionTableData.isSuccess = 1;
                        values.add(transactionTableData.insertValue());// bulk insert transaction

                        //update userPriority
                        String key = makeRegKey(transformSender(transactionTableData.sender), getSMSType(transactionTableData.smsType));
                        ArrayList<RegData> regDatas = mRegMap.get(key);
                        if(regDatas!=null) {
                            for(RegData regData : regDatas){
                                if(regData.regId == transactionTableData.regId) {
                                    regDataMap.put(regData.regId, regData);
                                    break;
                                }
                            }
                        }

                        //update card
                        if(mCardMap!=null) {
                            CardTableData cardTableData = mCardMap.get(makeCardKey(transactionTableData.cardTableData));
                            if(cardTableData!=null) {
                                if(transactionTableData.cardTableData.cardId == cardTableData.cardId) {
                                    cardMap.put(cardTableData.cardId, cardTableData);
                                }
                            }
                        }
                    }
                }


                if(tran.smsType == Constants.SMSType.SMS.ordinal()) {

                    if(maxSMSId < tran.smsId) {
                        maxSMSId = tran.smsId;
                    }


                } else if(tran.smsType == Constants.SMSType.MMS.ordinal()){
                    if(maxMMSId < tran.smsId) {
                        maxMMSId = tran.smsId;
                    }
                }

            }

            //update card
            for( Map.Entry<Integer, CardTableData> elem : cardMap.entrySet() ){
                ContentValues values1 = ParserReaderContract.CardTable.populateContent(elem.getValue());
                String[] selectionArgs = { elem.getKey()+"" };
                mParserDao.update(ParserReaderContract.CardTable.TABLE_NAME, values1, ParserReaderContract.CardTable._ID + "= ?", selectionArgs);
            }


            //update userPriority
            for( Map.Entry<Integer, RegData> elem : regDataMap.entrySet() ){
                ContentValues regValues = new ContentValues();
                regValues.put(ParserReaderContract.RegExpressionTable.COLUMN_USER_PRIORITY, elem.getValue().userPriority);
                mParserDao.update(ParserReaderContract.RegExpressionTable.TABLE_NAME, regValues, ParserReaderContract.RegExpressionTable.COLUMN_REG_ID+"="+elem.getKey(), null);
            }


            //bulk insert transaction
            if(!values.isEmpty()) {
                LOGI(TAG, INSERT_TRANSACTION+ TextUtils.join(",", values));
                mParserDao.wdb.execSQL(INSERT_TRANSACTION+ TextUtils.join(",", values));
                values.clear();
            }

            if(maxSMSId != 0)
                PrefUtils.getInstance(mContext).saveIntValue(PrefUtils.MAX_SMS_ID, maxSMSId);

            if(maxMMSId != 0)
                PrefUtils.getInstance(mContext).saveIntValue(PrefUtils.MAX_MMS_ID, maxMMSId);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void updateUserPriorityWithBulk(String repSender, int smsType) {

        if(mRegMap != null) {
            String key = makeRegKey(repSender, getSMSType(smsType));
            ArrayList<RegData> regDatas = mRegMap.get(key);
            if(regDatas!=null) {
                Collections.sort(regDatas, new Utils.RegComparator());
                mRegMap.put(key, regDatas);
            }
        }
    }

    void onBulkComplete() {

        mRegMap = null;
        mSendersMap = null;
        mDuplMap = null;
        mMovingAssetMap = null;
        mOffsetMap = null;
        mTransactionMap = null;
        mCardMap = null;
        finSmsParser.clearFinanceRuleCashes();

    }


    /**************************************      ONE         *************************************************/

    /**
     * is_success = 0 인 리스트 함께 보냄
     * 0. sender 필터
     * 1. 정규표현식을 통한 데이터 추출
     * 2. 중복 데이터 확인
     * 3. 중복 x 상쇄 Tran 확인
     * 4. 중복 x 상쇄 x 자산이동 Tran 확인
     * 5. 잔액 변경

     * @param sms 수신된 문자
     * @return 파싱된 Tran List
     */

    ParserResult parse(SMS sms) {

        String repSender = "";
        ParserResult parserResult = new ParserResult();

        try {

            TransactionTableData transaction;
            TransactionTableData offsetTran = null;
            TransactionTableData movingAssetTran = null;

            if("com.kakao.talk".equals(sms.getSender()) && !mParserDao.isBankMsgWithKakao(sms.getTitle())) {
                parserResult.resultCode = ResultCode.NOT_PARSED;
                throw new Exception("not parsed");
            }

            //0. sender 필터
            repSender = mParserDao.getRepSender(sms.getSender(), sms.getDisplaySender());
            if(repSender == null) {
                repSender = transformRepSender(sms.getFullSms());
            }
            LOGI(TAG, "0. sender 대표 필터" + repSender);
            if (repSender == null) {
                parserResult = setNotParsedResult(sms, false, parserResult);
                throw new Exception("repSender null");
            }


            //1. 정규표현식을 통한 데이터 확인
            LOGI(TAG, "1. 정규표현식을 통한 데이터 확인");
            transaction = getTransactionWithReg(sms, repSender);
            if (transaction == null) {
                LOGI(TAG, "1. 정규표현식을 통한 데이터 확인: FALSE");
                LOGI(TAG, "NOT PARSED"+sms.toString());
                parserResult = setNotParsedResult(sms, true, parserResult);
                throw new Exception("reg null");

            }

            LOGI(TAG, "1. 정규표현식을 통한 데이터 확인: TRUE" + transaction.toString());


            //노티 연속 데이터 확인
//        if(sms.getSmsType() == Constants.SMSType.NOTIFICATION.ordinal() && //  시럽은 제거
            if(sms.shouldIgnoreDate()) {
                if(mParserDao.isNotiDuplicate(transaction.fullSms, transaction.sender, transaction.smsType)) {
                    parserResult = setNotParsedResult(sms, false, parserResult);
                    throw new Exception("duplicate");
                }
            } else {
                if(mParserDao.isNotiDuplicate(transaction.smsDate, transaction.fullSms, transaction.sender, transaction.smsType)) {
                    parserResult = setNotParsedResult(sms, false, parserResult);
                    throw new Exception("duplicate");
                }
            }

            //2. 중복 데이터 확인
            LOGI(TAG, "2. 중복 데이터 확인");
            TransactionTableData duplicationTran = mParserDao.getDuplicationTran(transaction, mRegHandler);

            //중복
            if (duplicationTran != null) {

                LOGI(TAG, "2. 중복 데이터 확인: TRUE" + duplicationTran.toString());
                //중복인 경우 우선순위에 따라 정보 변경
                transaction = mRegHandler.combineTran(duplicationTran, transaction);

            } else {
                //중복 아님
                LOGI(TAG, "2. 중복 데이터 확인: FALSE");

                //상쇄 확인
                offsetTran = mParserDao.getOffsetTran(transaction);
                LOGI(TAG, "3. 중복 x 상쇄 Tran 확인");

                //상쇄
                if (offsetTran != null) {

                    LOGI(TAG, "3. 중복 x -> 상쇄 Tran 확인: TRUE" + offsetTran.toString());
                    transaction = mRegHandler.combineTranWithOffset(offsetTran, transaction);

                } else {
                    //상쇄아님
                    LOGI(TAG, "3. 중복 x -> 상쇄 Tran 확인: FALSE");

                    //자산이동 확인
                    LOGI(TAG, "4. 중복 x -> 상쇄 x -> 자산이동 Tran 확인");

                    movingAssetTran = mParserDao.getMovingAssetTran(transaction);

                    //자산이동
                    if (movingAssetTran != null) {
                        LOGI(TAG, "4. 중복 x 상쇄 x 자산이동 Tran 확인: TRUE" + movingAssetTran.toString());

                        transaction = mRegHandler.combineTranWithMovingAsset(transaction);
                        movingAssetTran = mRegHandler.combineTranWithMovingAsset(movingAssetTran);

                    } else {

                        LOGI(TAG, "4. 중복 x 상쇄 x 자산이동 Tran 확인: FALSE");

                    }
                }
            }
            //잔액 업데이트
            if(transaction != null) transaction = mRegHandler.updateBalance(transaction);

            //목록 넣기
            ArrayList<Transaction> resultTrans = getResultTransactions(transaction, offsetTran, movingAssetTran);
            if(resultTrans.size() == 0) {
                parserResult = setNotParsedResult(sms, true, parserResult);
                throw new Exception("duplicate");

            }

            LOGI(TAG, "최종 리턴 사이즈:" + resultTrans.size());
            parserResult.transactions = resultTrans;
            parserResult.resultCode = ResultCode.SEND_TO_SERVER;

            return parserResult;
        } catch (Exception e) {
            // 파싱 안됨
            ParserResult finResult = parseFinancialProduct(sms, repSender);

            if(finResult.resultCode != ResultCode.SEND_TO_SERVER) {
                return new ParserResult(getResultCode(parserResult.resultCode, finResult.resultCode), null, null);
            } else {
                return finResult;
            }
        }

    }

    private int getResultCode(int tranResultCode, int finResultCode) {

        if(tranResultCode == ResultCode.NEED_TO_SYNC_PARSING_RULE ||
                tranResultCode == ResultCode.NEED_TO_SYNC_PARSING_RULE_NO_SENDER ||
                tranResultCode == ResultCode.NEED_TO_SEND_TO_SERVER) {

            return tranResultCode;
        } else {
            return finResultCode;
        }

    }

    ParserResult parseFinancialProduct(SMS sms, String repSender) {
        FinanceResponse<FinancialProduct> result = finSmsParser.parse(tenqube.parser.util.ParserMapper.INSTANCE.toFinancialSms(sms, repSender));

        if(result instanceof FinanceResponse.Success) {
            return new ParserResult(
                    ResultCode.SEND_TO_SERVER,
                    null,
                   tenqube.parser.util.ParserMapper.INSTANCE.fromFinParser(((FinanceResponse.Success) result).getData(), sms)
            );
        } else if (result instanceof FinanceResponse.NeedToUpdate) {
            return new ParserResult(
                    ResultCode.NEED_TO_SYNC_PARSING_RULE,
                    null,
                    null);

        } else {
            return new ParserResult(
                    ResultCode.NOT_PARSED,
                    null,
                    null);
        }

    }

    /***
     *
     * @param sms 입력 문자
     * @return 파싱안됨!, 룰동기화 필요, 파싱안되지만 내역변경 의심
     */
    private ParserResult setNotParsedResult(SMS sms, boolean isSender, ParserResult parserResult) {

        int cnt = 0;
        if(!TextUtils.isEmpty(sms.getFullSms())) {

            if(sms.getFullSms().contains("원")) {
                cnt++;
            }
            if(sms.getFullSms().contains(":")) {
                cnt++;
            }
            if(sms.getFullSms().contains("/")) {
                cnt++;
            }
            if(sms.getFullSms().contains(".")) {
                cnt++;
            }
        }

        if(cnt > 2) {
            if(isSender) {
                parserResult.resultCode = ResultCode.NEED_TO_SYNC_PARSING_RULE;
            } else {
                parserResult.resultCode = ResultCode.NEED_TO_SYNC_PARSING_RULE_NO_SENDER;
            }
        } else {
            parserResult.resultCode = ResultCode.NOT_PARSED;
        }

        return parserResult;
    }


    private ArrayList<Transaction> getResultTransactions(TransactionTableData transaction,
                                                         TransactionTableData offsetTran,
                                                         TransactionTableData movingAssetTran) {

        ArrayList<Transaction> resultTrans = new ArrayList<>();

        try {
            ArrayList<TransactionTableData> transactions = new ArrayList<>();
            if(transaction != null)
                transactions.add(transaction);
            if(offsetTran != null)
                transactions.add(offsetTran);
            if(movingAssetTran != null)
                transactions.add(movingAssetTran);

            //리턴 맵핑
            for (TransactionTableData tran : transactions) {

                mParserDao.mergeTransaction(tran);
                Transaction resultTran = mMapper.transform(tran);
                if(resultTran!=null) {
                    LOGI(TAG, "result"+resultTran.toString());
                    resultTrans.add(resultTran);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultTrans;
    }


    /**
     * 정규표현식을 통한 Transaction 추출
     *
     * 1. getRegDatas map.get(sender) == null? where sender and smsType
     * 2. matching start
     * 3. matched -> TransactionTableData , cardTableData, 데이터 셋팅
     *
     * @param sms 수신된 문자 or 알림
     * @return 파싱된 Transaction
     */
     private TransactionTableData getTransactionWithReg(SMS sms, String repSender) throws SQLException{

         LOGI(TAG, "getTransactionWithReg start" + sms.toString());
         TransactionTableData parsedTran;

         ArrayList<RegData> regDatas = mParserDao.getRegDatas(repSender, sms.getSmsType());

         parsedTran = mRegHandler.getTransactionWithReg(regDatas, sms);
         if(parsedTran != null) {

             parsedTran.cardTableData = mParserDao.getCard(parsedTran.cardTableData);
             if(parsedTran.userPriority < 10){
                 mParserDao.updateUserPriority(parsedTran.regId, parsedTran.userPriority);
             }

         }

         return  parsedTran;
    }

    void deleteOldTransaction(String smsDate) {

        LOGI(TAG, "deleteOldTransaction");
        Calendar cal = DateUtil.convertStringToCalendarFULL(smsDate);
        cal.add(Calendar.MONTH, -2);
        mParserDao.delete(ParserReaderContract.TransactionsTable.TABLE_NAME, ParserReaderContract.TransactionsTable.COLUMN_SMS_DATE + "<'"+ DateUtil.getStringDateAsYYYYMMddHHmmss(cal)+"'", null);

    }

    void updateIsSuccess(ArrayList<Transaction> transactions, int isSuccess) throws SQLException  {

        LOGI(TAG, "updateIsSuccess");
        ArrayList<String> identifierList = new ArrayList<>();
        for (Transaction transaction : transactions) {
            identifierList.add("'" + transaction.identifier + "'");
        }
        ContentValues values = new ContentValues();
        values.put(ParserReaderContract.TransactionsTable.COLUMN_IS_SUCCESS, isSuccess);
        mParserDao.update(ParserReaderContract.TransactionsTable.TABLE_NAME, values, ParserReaderContract.TransactionsTable.COLUMN_IDENTIFIER + IN + "("+ TextUtils.join(",", identifierList)+")", null);
    }


    /**
     * regId 가 같으면 업데이트 다르면 insert
     * isDelete == 1 이면 삭제
     *
     * @param parsingRule 파싱룰 데이터
     */
    void syncParsingRule(ParsingRule parsingRule, int bulkSize) {

        try {
            LOGI(TAG, "syncParsingRule");
            mParserDao.mergeRegDatas(parsingRule.regDatas, bulkSize);
            mParserDao.mergeSenders(parsingRule.senders, bulkSize);
            mParserDao.mergeBanks(parsingRule.banks, bulkSize);
            finSmsParser.saveFinanceRules(tenqube.parser.util.ParserMapper.INSTANCE.toFinItems(parsingRule.financialProductRules));
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

    }

    /**
     * transactions 테이블 초기화, senders delete where id<0
     */
    void initDb() throws SQLException {
        LOGI(TAG, "initDb");
        mParserDao.initDb();
    }

    /**
     * 사용자 추가 번호 아이디는 최소아이디 값 -1 (음수 처리)
     * 1.get senders min id
     * 2. min id > 0 ? id = -1 : id = min_id -1
     * 3. insert
     * @param number 전화번호
     */
    void addTestOriginNumber(String number) throws SQLException {

        LOGI(TAG, "addTestOriginNumber");
        int minId = mParserDao.getMinSendersId();
        int id = minId >= 0 ? -1 : minId - 1;

        Sender sender = new Sender();
        sender.senderId = id;
        sender.sender = transformSender(number);
        sender.repSender = ALL;
        sender.smsType = Constants.SMSType.SMS.ordinal();

        ContentValues values = ParserReaderContract.SendersTable.populateContent(sender);
        String[] selectionArgs = { sender.sender };
        long suc = mParserDao.update(ParserReaderContract.SendersTable.TABLE_NAME, values, ParserReaderContract.SendersTable.COLUMN_SENDER + "= ?", selectionArgs);
        if(suc == 0) {
            mParserDao.insert(ParserReaderContract.SendersTable.TABLE_NAME, values);
        }

    }

}

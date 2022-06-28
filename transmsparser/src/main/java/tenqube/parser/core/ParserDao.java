package tenqube.parser.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import tenqube.parser.constants.Constants;
import tenqube.parser.model.Bank;
import tenqube.parser.model.RegData;
import tenqube.parser.model.Sender;
import tenqube.parser.model.Transaction;

import static tenqube.parser.constants.Constants.ALL;
import static tenqube.parser.constants.Constants.LG_PAY;
import static tenqube.parser.constants.Constants.NO;
import static tenqube.parser.constants.Constants.SAMSUNG_PAY;
import static tenqube.parser.constants.Constants.YES;
import static tenqube.parser.core.ParserService.mIsDebug;
import static tenqube.parser.core.Utils.getSMSType;
import static tenqube.parser.core.Utils.transformSender;
import static tenqube.parser.util.LogUtil.LOGE;
import static tenqube.parser.util.LogUtil.LOGI;

class ParserDao extends BaseQueryHelper {

    ParserDao(Context context) throws SecurityException, SQLException {
        super(context);
    }

    boolean isBankMsgWithKakao(String title) {
        LOGI(TAG, "isBankMsgWithKakao");

        if(TextUtils.isEmpty(title))
            return false;

        Cursor c = null;
        String query =
                SELECT + EXISTS + "(" +
                         SELECT + " 1 " +
                         FROM + ParserReaderContract.BanksTable.TABLE_NAME +
                         WHERE + ParserReaderContract.BanksTable.COLUMN_NAME + " = '"+ title +"' " + OR +
                         ParserReaderContract.BanksTable.COLUMN_NAME + " like '%"+ title +"%' "
                        + ")";
        try {
            c = runQuery(query);
            if (c != null) {
                if (c.moveToFirst()) {
                    return c.getInt(0) == 1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (c != null)
                c.close();
        }
        return false;
    }


    String getRepSender(String sender) {

        LOGI(TAG, "getRepSender");

        String transformSender = transformSender(sender);

        Cursor c = null;
        String query =
                SELECT + ParserReaderContract.SendersTable.COLUMN_REP_SENDER +
                FROM + ParserReaderContract.SendersTable.TABLE_NAME +
                WHERE + ParserReaderContract.SendersTable.COLUMN_SENDER + IN + "('"+sender+"','"+transformSender+"')" +
                LIMIT + "1";

        LOGI(TAG, "getRepSender query:" + query);

        try {

            c = runQuery(query);
            if (c != null) {
                if (c.moveToFirst()) {
                    return c.getString(c.getColumnIndex(ParserReaderContract.SendersTable.COLUMN_REP_SENDER));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (c != null)
                c.close();
        }

        return null;

    }

    ArrayList<RegData> getRegDatasWithBulk(String repSender, int smsType) {

        LOGI(TAG, "getRegDatasWithBulk");
        Cursor c = null;
        ArrayList<RegData> regDatas = new ArrayList<>();

        String query =
                SELECT + "*" +
                FROM + ParserReaderContract.RegExpressionTable.TABLE_NAME +
                WHERE +ParserReaderContract.RegExpressionTable.COLUMN_SMS_TYPE + IN +" (" +smsType + ", 3 )";

        query += AND + ParserReaderContract.RegExpressionTable.COLUMN_SENDER +" = '" + repSender +"'";

        query += ORDER_BY + ParserReaderContract.RegExpressionTable.COLUMN_PRIORITY + DESC + "," +
                ParserReaderContract.RegExpressionTable.COLUMN_USER_PRIORITY + DESC;
        try{
            c = runQuery(query);
            if (c != null) {
                if (c.moveToFirst()) {
                    regDatas = new ArrayList<>();
                    while (!c.isAfterLast()) {

                        regDatas.add(ParserReaderContract.RegExpressionTable.populateModel(c, mContext));
                        c.moveToNext();
                    }
                }
            }
        } catch (SQLiteException e){
            e.printStackTrace();
        } finally {
            if (c != null)
                c.close();
        }

        return regDatas;

    }

    Map<String, String> getSendersMap(){

        LOGI(TAG, "setSenders");
        HashMap<String, String> mSendersMap = new HashMap();
        Cursor c = null;
        String query =
                SELECT + ParserReaderContract.SendersTable.COLUMN_SENDER +"," + ParserReaderContract.SendersTable.COLUMN_REP_SENDER +
                        FROM + ParserReaderContract.SendersTable.TABLE_NAME;

        try {
            c = runQuery(query);
            if (c != null) {
                if (c.moveToFirst()) {
                    while (!c.isAfterLast()) {
                        String key = c.getString(c.getColumnIndex(ParserReaderContract.SendersTable.COLUMN_SENDER));
                        String repSender = c.getString(c.getColumnIndex(ParserReaderContract.SendersTable.COLUMN_REP_SENDER));
                        if(!repSender.equals(Constants.ALL)) {
                            mSendersMap.put(key,repSender);
                        }
                        c.moveToNext();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (c != null)
                c.close();
        }

        return mSendersMap;

    }

    /**
     * 대표 sender 구하기
     *
     * @param sender 수신된 전화번호 또는 패키지
     * @return 대표 Sender
     */
    String getRepSender(String sender, String displaySender) {

        LOGI(TAG, "getRepSender");
        Cursor c = null;
        String query = "";
        if (TextUtils.isEmpty(displaySender) ||
                sender.equals(displaySender)) {

            String transformSender = transformSender(sender);
            query +=
                    SELECT + ParserReaderContract.SendersTable.COLUMN_REP_SENDER +
                            FROM + ParserReaderContract.SendersTable.TABLE_NAME +
                            WHERE + ParserReaderContract.SendersTable.COLUMN_SENDER + IN + "('"+sender+"','"+transformSender+"')" +
                            LIMIT + "1";
        } else {

            String transformSender = transformSender(sender);
            String transformDisplaySender = transformSender(displaySender);
            query +=
                    SELECT + ParserReaderContract.SendersTable.COLUMN_REP_SENDER +
                            FROM + ParserReaderContract.SendersTable.TABLE_NAME +
                            WHERE + ParserReaderContract.SendersTable.COLUMN_SENDER + IN + "('"+sender+"','"+transformSender+"','"+displaySender+"','"+transformDisplaySender+"')" +
                            LIMIT + "1";

        }
        LOGI(TAG, "getRepSender query:" + query);
        try {
            c = runQuery(query);
            if (c != null) {
                if (c.moveToFirst()) {
                    return c.getString(c.getColumnIndex(ParserReaderContract.SendersTable.COLUMN_REP_SENDER));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (c != null)
                c.close();
        }

        return null;

    }


    /**
     * 실패 내역 뽑아오기
     * @return fail tran list
     */
    ArrayList<Transaction> getFailTransaction (ParserMapper mapper) {

        LOGI(TAG, "getFailTransaction");

        ArrayList<Transaction> failTrans = new ArrayList<>();

        Cursor c = null;
        String query =
                SELECT + "*" +
                        FROM + getJoinTable() +
                        WHERE+  ParserReaderContract.TransactionsTable.ALIAS + ParserReaderContract.TransactionsTable.COLUMN_IS_SUCCESS + "=" + Constants.NO +
                        ORDER_BY + ParserReaderContract.TransactionsTable.ALIAS+ ParserReaderContract.TransactionsTable.COLUMN_SPENT_DATE + ASC;

        try {
            c = runQuery(query);
            if (c != null) {
                if (c.moveToFirst()) {
                    while (!c.isAfterLast()) {

                        TransactionTableData failTran = ParserReaderContract.TransactionsTable.populateModel(c);
                        failTran.cardTableData = ParserReaderContract.CardTable.populateModel(c);
                        Transaction tran = mapper.transform(failTran);
                        if(tran!=null)
                            failTrans.add(tran);
                        c.moveToNext();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (c != null)
                c.close();
        }

        return failTrans;

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
    CardTableData getCard(CardTableData parsedCard) throws SQLException {

        LOGI(TAG, "getCard");

        Cursor c = null;
        String query =
                SELECT + "*" +
                        FROM + ParserReaderContract.CardTable.TABLE_NAME +
                        WHERE + ParserReaderContract.CardTable.COLUMN_CARD_NAME + "='" + parsedCard.cardName + "'" +
                        AND + ParserReaderContract.CardTable.COLUMN_CARD_NUM + "='" + parsedCard.cardNum + "'" +
                        AND + ParserReaderContract.CardTable.COLUMN_CARD_TYPE + "=" + parsedCard.cardType +
                        AND + ParserReaderContract.CardTable.COLUMN_CARD_SUB_TYPE + "=" + parsedCard.cardSubType;
        try {

            c = runQuery(query);
            if (c != null) {
                if (c.moveToFirst()) {
                    while (!c.isAfterLast()) {

                        parsedCard = ParserReaderContract.CardTable.populateModel(c);
                        c.moveToNext();

                    }
                }

            } else {
                parsedCard.cardId = (int)insert(ParserReaderContract.CardTable.TABLE_NAME, ParserReaderContract.CardTable.populateContent(parsedCard));
            }

        } catch (SQLiteException e){
            e.printStackTrace();
        }finally {
            if (c != null)
                c.close();
        }

        return parsedCard;

    }

    void updateUserPriority(int id, int userPriority) {

        ContentValues values = new ContentValues();
        values.put(ParserReaderContract.RegExpressionTable.COLUMN_USER_PRIORITY, ++userPriority);
        update(ParserReaderContract.RegExpressionTable.TABLE_NAME, values, ParserReaderContract.RegExpressionTable.COLUMN_REG_ID+"="+id, null);
    }

    ArrayList<RegData> getRegDatas(String sender, int smsType) {

        LOGI(TAG, "getRegDatas");
        Cursor c = null;
        ArrayList<RegData> regDatas = null;

        smsType = getSMSType(smsType);
        String query =
                SELECT + "*" +
                        FROM + ParserReaderContract.RegExpressionTable.TABLE_NAME +
                        WHERE +ParserReaderContract.RegExpressionTable.COLUMN_SMS_TYPE + IN +" (" +smsType + ", 3 )";


        query += (ALL.equals(sender) ||(smsType==0))?
                ""
                :
                AND + ParserReaderContract.RegExpressionTable.COLUMN_SENDER +" = '" + sender + "'";

        query += ORDER_BY + ParserReaderContract.RegExpressionTable.COLUMN_PRIORITY + DESC + "," +
                ParserReaderContract.RegExpressionTable.COLUMN_USER_PRIORITY + DESC;
        try{
            LOGI(TAG, "getRegDatas query:" + query);

            c = runQuery(query);
            if (c != null) {
                if (c.moveToFirst()) {
                    regDatas = new ArrayList<>();
                    while (!c.isAfterLast()) {

                        regDatas.add(ParserReaderContract.RegExpressionTable.populateModel(c, mContext));
                        c.moveToNext();
                    }
                }
            }

        } catch (SQLiteException e){
            LOGE(TAG, e.toString());
        }finally {
            if (c != null)
                c.close();
        }

        return regDatas;

    }

    /**
     * 중복 Transaction 추출 함수
     *
     * 조건
     * 1. 새롭게 파싱된 spentDate between  (tran.date - 15분) and (tran.date +5분)
     * 2. spentMoney == tran.금액
     * 3. dwType == tran.dwType
     *
     * @param tran 파싱된 Transaction
     * @return 중복된 Transaction
     */
    TransactionTableData getDuplicationTran(TransactionTableData tran, RegHandler mRegHandler) throws SQLException {
        LOGI(TAG, "getDuplicationTran");

        TransactionTableData duplicationTran;

        Cursor c = null;

        Calendar cal = DateUtil.convertStringToCalendarFULL(tran.spentDate);
        cal.add(Calendar.MINUTE, 5);
        String endDate = DateUtil.getStringDateAsYYYYMMddHHmm(cal);

        if (SAMSUNG_PAY.equals(tran.cardTableData.cardName) || LG_PAY.equals(tran.cardTableData.cardName)) {
            cal.add(Calendar.HOUR_OF_DAY, -13);
        } else {
            cal.add(Calendar.MINUTE, -15);
        }

        String startDate = DateUtil.getStringDateAsYYYYMMddHHmm(cal);

        String query =

                SELECT + "*" +
                        FROM + getJoinTable()+
                        WHERE +
                        "strftime(" + YYYY_MM_DD_H_M + "," + ParserReaderContract.TransactionsTable.COLUMN_SPENT_DATE + ")>'" + startDate + "' " +
                        AND + " strftime(" + YYYY_MM_DD_H_M+ "," + ParserReaderContract.TransactionsTable.COLUMN_SPENT_DATE+ ")<='" + endDate + "' " +
                        AND + "(" +
                        "(" +
                        ParserReaderContract.TransactionsTable.COLUMN_SPENT_MONEY + "=" + tran.spentMoney+
                        AND + ParserReaderContract.TransactionsTable.COLUMN_DW_TYPE + "=" + tran.dwType+
                        ")" +
                        OR +
                        "(" +
                        ParserReaderContract.TransactionsTable.COLUMN_SPENT_MONEY + "=" + -1*tran.spentMoney +
                        AND + ParserReaderContract.TransactionsTable.COLUMN_DW_TYPE + "!=" + tran.dwType +
                        ")" +
                        ")" +
                        ORDER_BY + ParserReaderContract.TransactionsTable.COLUMN_SPENT_DATE + DESC +
                        LIMIT + " 1 ";

        try {
            c = runQuery(query);

            if(c != null){
                if (c.moveToFirst()) {
                    while (!c.isAfterLast()) {

                        duplicationTran = ParserReaderContract.TransactionsTable.populateModel(c);
                        duplicationTran.cardTableData = ParserReaderContract.CardTable.populateModel(c);
                        //처음 잡힌 정보에따른 중복 체크 함수
                        if(mRegHandler.isDuplicate(duplicationTran.originInfos, tran.originInfos)) {
                            return  duplicationTran;
                        }

                        c.moveToNext();
                    }
                }
            }

        } catch (SQLiteException e){
            LOGE(TAG, e.toString());
        } finally {
            if (c != null)
                c.close();
        }

        return  null;

    }

    /**
     * 승인 인식 후 취소 ( 합 == 0) 와 같이 상쇄 Transaction 추출 함수
     *
     * 조건
     * and 파싱된 금액 < 0 취소 결제
     * and spentDate between tran.spentDate-2달 and ran.spentDate+1일
     * and spentMoney == |tran.spentMoney|
     * and dwType == tran.dwType
     * and isOffset == 0
     * and (keyword == tran.keyword || cardName like '%tran.cardName%' || cardName like '%변형 tran.cardName%'
     *
     * order by spentDate desc (최신 데이터)
     * limit 1  (1개)
     *
     *
     * @param tran 파싱된 Transaction
     * @return 상쇄된 Transaction
     */
    TransactionTableData getOffsetTran(TransactionTableData tran) throws SQLException {
        LOGI(TAG, "getOffsetTran");

        if (tran.spentMoney < 0) {

            Cursor c = null;
            Calendar cal= DateUtil.convertStringToCalendarFULL(tran.spentDate);
            cal.add(Calendar.DATE, 1);
            String endDate = DateUtil.getStringDateAsYYYYMMddHHmmss(cal);
            cal.add(Calendar.MONTH, -2);
            String startDate = DateUtil.getStringDateAsYYYYMMddHHmmss(cal);

            String query =
                    SELECT + "*" +
                    FROM + getJoinTable() +
                    WHERE+  ParserReaderContract.TransactionsTable.ALIAS + ParserReaderContract.TransactionsTable.COLUMN_SPENT_DATE+ " >='" + startDate+"' " +
                    AND + ParserReaderContract.TransactionsTable.ALIAS + ParserReaderContract.TransactionsTable.COLUMN_SPENT_DATE + " < '" + endDate+ "'" +
                    AND + ParserReaderContract.TransactionsTable.ALIAS + ParserReaderContract.TransactionsTable.COLUMN_SPENT_MONEY + "="+ -1*tran.spentMoney +
                    AND + ParserReaderContract.TransactionsTable.ALIAS + ParserReaderContract.TransactionsTable.COLUMN_DW_TYPE + "=" + tran.dwType +
                    AND + ParserReaderContract.TransactionsTable.ALIAS + ParserReaderContract.TransactionsTable.COLUMN_IS_OFFSET+ " = " + NO +
                    AND +
                    "(" +
                    ParserReaderContract.TransactionsTable.ALIAS + ParserReaderContract.TransactionsTable.COLUMN_KEYWORD+ "='"+ tran.keyword.trim() + "'"+
                    OR +
                    ParserReaderContract.CardTable.ALIAS + ParserReaderContract.CardTable.COLUMN_CARD_NAME + " like '%" + tran.cardTableData.cardName + "%'" +
                    OR +
                    ParserReaderContract.CardTable.ALIAS + ParserReaderContract.CardTable.COLUMN_CARD_NAME + " like '%" + Utils.transformCardName(tran.cardTableData.cardName) + "%'" +
                    ")" +

                    ORDER_BY + ParserReaderContract.TransactionsTable.ALIAS+ ParserReaderContract.TransactionsTable.COLUMN_SPENT_DATE + DESC+
                    LIMIT + "1";

            LOGI(TAG, "offset query"+ query);


            try{
                c = runQuery(query);
                if(c != null){
                    if (c.moveToFirst()) {
                        TransactionTableData offsetTran = ParserReaderContract.TransactionsTable.populateModel(c);
                        offsetTran.isOffset = YES;
                        offsetTran.cardTableData = ParserReaderContract.CardTable.populateModel(c);
                        return offsetTran;
                    }
                }
            } catch (SQLiteException e){

                e.printStackTrace();

            } finally {
                if (c != null)
                    c.close();
            }

        }
        return null;
    }


    /**
     * 자산이동 Transaction 추출 함수
     * 조건
     * 1. 파싱된 cardType 계좌
     * 2. spentDate between tran.spentDate -3분 and tran.spentDate +3분
     * 3. dwType != tran.dwType (입 출금 반대)
     * 4. spentMoney == tran.spentMoney
     * 5. categoryCode not in (98, 88) 입출금 자산이동 카테고리
     * 6. cardNum != tran.cardNum
     * 7. cardType == 2 (계좌)
     * @param tran 파싱된 Transaction
     * @return 자산이동 Transaction
     */
    TransactionTableData getMovingAssetTran(@NotNull TransactionTableData tran) throws SQLException {

        TransactionTableData movingAssetTran;
        LOGI(TAG, "getMovingAssetTran");
        //앞+ 3분 뒤 3분 and dwTyp!= spent_money= cardName+cardNum cardType bank
        //spentMoney, cardType
        if (tran.cardTableData.cardType == Constants.CardType.BANK_ACCOUNT.ordinal()) {


            Calendar cal= DateUtil.convertStringToCalendarFULL(tran.spentDate);
            cal.add(Calendar.MINUTE, 3);
            String endDate= DateUtil.getStringDateAsYYYYMMddHHmm(cal);
            cal.add(Calendar.MINUTE, -6);
            String startDate= DateUtil.getStringDateAsYYYYMMddHHmm(cal);
            String query =
                    SELECT + "*" +
                    FROM + getJoinTable() +
                    WHERE +
                    "strftime(" + YYYY_MM_DD_H_M+ "," + ParserReaderContract.TransactionsTable.COLUMN_SPENT_DATE+ ")>'" + startDate+ "' " +
                    AND + " strftime(" + YYYY_MM_DD_H_M + "," + ParserReaderContract.TransactionsTable.COLUMN_SPENT_DATE+ ")<='" + endDate+ "' " +
                    AND + ParserReaderContract.TransactionsTable.COLUMN_DW_TYPE+"!=" + tran.dwType +
                    AND + ParserReaderContract.TransactionsTable.COLUMN_SPENT_MONEY+"=" + tran.spentMoney +
                    AND + ParserReaderContract.TransactionsTable.COLUMN_CATEGORY_CODE + " NOT IN(98,88)" +
                    AND + "(" + ParserReaderContract.CardTable.COLUMN_CARD_NAME + "!='" + tran.cardTableData.cardName+ "'" +
                    OR + ParserReaderContract.CardTable.COLUMN_CARD_NUM + "!='" + tran.cardTableData.cardNum+ "'" +
                    ")" +
                    AND + ParserReaderContract.CardTable.COLUMN_CARD_TYPE + "="+Constants.CardType.BANK_ACCOUNT.ordinal() +
                    LIMIT + " 1";

            Cursor c = null;
            try {
                c = runQuery(query);
                if(c != null){
                    if (c.moveToFirst()) {
                        movingAssetTran = ParserReaderContract.TransactionsTable.populateModel(c);
                        movingAssetTran.cardTableData = ParserReaderContract.CardTable.populateModel(c);
                        return movingAssetTran;
                    }
                }
            } catch (SQLiteException e){
                LOGE(TAG, e.toString());
            } finally {
                if (c != null)
                    c.close();
            }
        }
        return null;
    }

    void mergeTransaction(TransactionTableData tran) throws SQLException{

        LOGI(TAG, "mergeTransaction");
        if (tran == null)
            return;

        ContentValues values = ParserReaderContract.TransactionsTable.populateContent(tran);
        String[] selectionArgs = { tran.identifier+"" };

        long suc = update(ParserReaderContract.TransactionsTable.TABLE_NAME, values, ParserReaderContract.TransactionsTable.COLUMN_IDENTIFIER + "= ?", selectionArgs);
        if (suc == 0) {
            insert(ParserReaderContract.TransactionsTable.TABLE_NAME, values);
        }

        values = ParserReaderContract.CardTable.populateContent(tran.cardTableData);
        selectionArgs = new String[]{ tran.cardTableData.cardId+"" };

        suc = update(ParserReaderContract.CardTable.TABLE_NAME, values, ParserReaderContract.CardTable._ID + "= ?", selectionArgs);
        if (suc == 0) {
            insert(ParserReaderContract.CardTable.TABLE_NAME, values);
        }

    }

    /**
     * transactions 테이블 초기화, senders delete where id<0
     */
    void initDb() throws SQLException {
        LOGI(TAG, "initDb");
        PrefUtils.getInstance(mContext).clear();
        DatabaseHelper.getInstance(mContext).onCreate(wdb);
    }

    void initTransactions() throws SQLiteException {
        PrefUtils.getInstance(mContext).saveIntValue(PrefUtils.MAX_SMS_ID, 0);
        PrefUtils.getInstance(mContext).saveIntValue(PrefUtils.MAX_MMS_ID, 0);

        wdb.execSQL(ParserReaderContract.BanksTable.SQL_DELETE_ENTRIES);
        wdb.execSQL(ParserReaderContract.BanksTable.SQL_CREATE_ENTRIES);
        wdb.execSQL(ParserReaderContract.BanksTable.indexing);

        wdb.execSQL(ParserReaderContract.CardTable.SQL_DELETE_ENTRIES);
        wdb.execSQL(ParserReaderContract.CardTable.SQL_CREATE_ENTRIES);
        wdb.execSQL(ParserReaderContract.CardTable.indexing);

        wdb.execSQL(ParserReaderContract.TransactionsTable.SQL_DELETE_ENTRIES);
        wdb.execSQL(ParserReaderContract.TransactionsTable.SQL_CREATE_ENTRIES);
        wdb.execSQL(ParserReaderContract.TransactionsTable.indexing);
        wdb.execSQL(ParserReaderContract.TransactionsTable.indexing_2);
        wdb.execSQL(ParserReaderContract.TransactionsTable.indexing_3);
    }


    int getMinSendersId(){

        LOGI(TAG, "getMinSendersId");

        Cursor c = null;
        String query =
                SELECT + "MIN("+ ParserReaderContract.SendersTable.COLUMN_SENDER_ID+")" +
                FROM + ParserReaderContract.SendersTable.TABLE_NAME;

        try {
            c = runQuery(query);
            if (c != null) {
                if (c.moveToFirst()) {

                    return c.getInt(0);

                }
            }
        } catch (SQLException e) {
            LOGE(TAG, e.toString());
        } finally {
            if (c != null)
                c.close();
        }

        return 0;

    }



    void mergeRegDatas(ArrayList<RegData> regDatas, int bulkSize) throws SQLException {

        LOGI(TAG, "mergeRegDatas");
        if(regDatas == null)
            return;

        ArrayList<String> deleteIds = new ArrayList<>();
        ArrayList<String> insertValues = new ArrayList<>();


        for (RegData regData : regDatas) {

            if (regData.isDelete == YES) {
                deleteIds.add(regData.regId + "");
            } else {
                LOGI(TAG, "regDatas:" + regData.getInsertValue());
                insertValues.add(regData.getInsertValue());
            }

            if(deleteIds.size() == bulkSize) {
                delete(ParserReaderContract.RegExpressionTable.TABLE_NAME, ParserReaderContract.RegExpressionTable.COLUMN_REG_ID + IN + "(" +TextUtils.join(",", deleteIds) + ")", null);
                deleteIds.clear();
            }

            if(insertValues.size() == bulkSize) {
                String insertQuery = ParserReaderContract.RegExpressionTable.INSERT_REG + TextUtils.join(",", insertValues);
                LOGI(TAG, "regDatas: insertQuery" + insertQuery);

                wdb.execSQL(insertQuery);
                insertValues.clear();
            }

        }

        if(deleteIds.size() != 0) {
            delete(ParserReaderContract.RegExpressionTable.TABLE_NAME, ParserReaderContract.RegExpressionTable.COLUMN_REG_ID + IN + "(" +TextUtils.join(",", deleteIds) + ")", null);
            deleteIds.clear();
        }

        if(insertValues.size() != 0) {
            String insertQuery = ParserReaderContract.RegExpressionTable.INSERT_REG + TextUtils.join(",", insertValues);
            wdb.execSQL(insertQuery);
            insertValues.clear();
        }

    }

    void mergeSenders(ArrayList<Sender> senders, int bulkSize) throws SQLException{

        LOGI(TAG, "mergeSenders");
        if (senders == null) {
            return;
        }

        ArrayList<String> deleteIds = new ArrayList<>();
        ArrayList<String> insertValues = new ArrayList<>();


        for (Sender sender : senders) {

            if (sender.isDelete == YES) {
                deleteIds.add(sender.senderId + "");
            } else {
                insertValues.add(sender.getInsertValue());
            }

            if(deleteIds.size() == bulkSize) {
                delete(ParserReaderContract.SendersTable.TABLE_NAME, ParserReaderContract.SendersTable.COLUMN_SENDER_ID + IN + "(" +TextUtils.join(",", deleteIds) + ")", null);
                deleteIds.clear();
            }

            if(insertValues.size() == bulkSize) {
                String insertQuery = ParserReaderContract.SendersTable.INSERT_SENDER + TextUtils.join(",", insertValues);
                wdb.execSQL(insertQuery);
                insertValues.clear();
            }
        }

        if(deleteIds.size() != 0) {
            delete(ParserReaderContract.SendersTable.TABLE_NAME, ParserReaderContract.SendersTable.COLUMN_SENDER_ID + IN + "(" +TextUtils.join(",", deleteIds) + ")", null);
            deleteIds.clear();
        }

        if(insertValues.size() != 0) {
            String insertQuery = ParserReaderContract.SendersTable.INSERT_SENDER + TextUtils.join(",", insertValues);
            wdb.execSQL(insertQuery);
            insertValues.clear();
        }
    }


    void mergeBanks(ArrayList<Bank> banks, int bulkSize) throws SQLException{

        LOGI(TAG, "mergeBanks");


        ArrayList<String> deleteIds = new ArrayList<>();
        ArrayList<String> insertValues = new ArrayList<>();
        if (banks == null) {
            return;
        }
        for (Bank bank : banks) {
            if (bank.isDelete == YES) {
                deleteIds.add(bank.id + "");
            } else {
                insertValues.add(bank.getInsertValue());
            }

            if(deleteIds.size() == bulkSize) {
                delete(ParserReaderContract.BanksTable.TABLE_NAME, ParserReaderContract.BanksTable.COLUMN_BANK_ID + IN + "(" +TextUtils.join(",", deleteIds) + ")", null);
                deleteIds.clear();
            }

            if(insertValues.size() == bulkSize) {
                String insertQuery = ParserReaderContract.BanksTable.INSERT_BANK + TextUtils.join(",", insertValues);
                wdb.execSQL(insertQuery);
                insertValues.clear();
            }
        }

        if(deleteIds.size() != 0) {
            delete(ParserReaderContract.BanksTable.TABLE_NAME, ParserReaderContract.BanksTable.COLUMN_BANK_ID + IN + "(" +TextUtils.join(",", deleteIds) + ")", null);
            deleteIds.clear();
        }

        if(insertValues.size() != 0) {
            String insertQuery = ParserReaderContract.BanksTable.INSERT_BANK + TextUtils.join(",", insertValues);
            wdb.execSQL(insertQuery);
            insertValues.clear();
        }

    }

    boolean isNotiDuplicate(String fullSms, String sender, int smsType){

        LOGI(TAG, "isNotiDuplicate");

        Cursor c = null;
        String query =
                SELECT + " * " +
                        FROM + ParserReaderContract.TransactionsTable.TABLE_NAME +
                        WHERE + ParserReaderContract.TransactionsTable.COLUMN_FULL_SMS + " = '" + fullSms + "'" + AND +
                        ParserReaderContract.TransactionsTable.COLUMN_SENDER + " = '" + sender + "'"+ AND +
                        ParserReaderContract.TransactionsTable.COLUMN_SMS_TYPE + " = " + smsType;


        LOGI(TAG, "isNotiDuplicate " + query);

        try {
            c = runQuery(query);
            if (c != null) {
                if (c.moveToFirst()) {
                    LOGI(TAG, "isNotiDuplicate : true");
                    return true;

                }
            }
        } catch (SQLException e) {
            LOGE(TAG, e.toString());
        } finally {
            if (c != null)
                c.close();
        }
        LOGI(TAG, "isNotiDuplicate : false");

        return false;

    }


    boolean isNotiDuplicate(String smsDate, String fullSms, String sender, int smsType){

        LOGI(TAG, "isNotiDuplicate");

        Cursor c = null;
        String query =
                SELECT + " * " +
                FROM + ParserReaderContract.TransactionsTable.TABLE_NAME +
                WHERE + ParserReaderContract.TransactionsTable.COLUMN_SMS_DATE + " = '" + smsDate + "' " + AND +
                        ParserReaderContract.TransactionsTable.COLUMN_FULL_SMS + " = '" + fullSms + "'" + AND +
                        ParserReaderContract.TransactionsTable.COLUMN_SENDER + " = '" + sender + "'"+ AND +
                        ParserReaderContract.TransactionsTable.COLUMN_SMS_TYPE + " = " + smsType;


                        LOGI(TAG, "isNotiDuplicate " + query);

        try {
            c = runQuery(query);
            if (c != null) {
                if (c.moveToFirst()) {
                    LOGI(TAG, "isNotiDuplicate : true");
                    return true;

                }
            }
        } catch (SQLException e) {
            LOGE(TAG, e.toString());
        } finally {
            if (c != null)
                c.close();
        }
        LOGI(TAG, "isNotiDuplicate : false");

        return false;

    }


}

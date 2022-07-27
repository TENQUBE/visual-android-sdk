package tenqube.transmsparser.core;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.text.TextUtils;

import java.util.Calendar;

import tenqube.transmsparser.constants.Constants;
import tenqube.transmsparser.model.Bank;
import tenqube.transmsparser.model.RegData;
import tenqube.transmsparser.model.Sender;


final class ParserReaderContract {

    private static final String TEXT_TYPE = " TEXT ";
    private static final String REAL_TYPE = " REAL ";
    private static final String INTEGER_TYPE = " INTEGER ";
    private static final String COMMA_SEP = " , ";
    private static final String DATE_TYPE = " DATETIME ";
    private static final String DEFAULT = " DEFAULT ";
    private static final String NOT_NULL = " NOT NULL ";
    private static final String PRIMARY_KEY=" PRIMARY KEY ";
    private static final String UNIQUE=" UNIQUE ";
    private static final String AUTOINCREMENT=" AUTOINCREMENT ";
    private static final String CREATE_TABLE=" CREATE TABLE ";
    private static final String CREATE_TABLE_IF_NOT_EXISTS = " CREATE TABLE IF NOT EXISTS ";
    private static final String DROP_TABLE_IF_EXISTS=" DROP TABLE IF EXISTS ";
    private static final String DEFAULT_TEXT=" 'none' ";
    private static final String DEFAULT_INT=" 0 ";
    private static final String DEFAULT_DATE=" current_timestamp ";

    private ParserReaderContract() {
    }


    static class RegExpressionTable implements BaseColumns {


        static final String TABLE_NAME = " REG_EXPRESSION";
        static final String COLUMN_REG_ID = "regId";
        static final String COLUMN_SENDER = "sender";
        static final String COLUMN_REG_EXPRESSION = "regExpression";

        static final String COLUMN_CARD_NAME = "cardName";
        static final String COLUMN_CARD_TYPE = "cardType";
        static final String COLUMN_CARD_SUB_TYPE = "cardSubType";
        static final String COLUMN_CARD_NUM = "cardNum";
        static final String COLUMN_SPENT_MONEY = "spentMoney";
        static final String COLUMN_SPENT_DATE = "spentDate";

        static final String COLUMN_KEYWORD = "keyword";

        static final String COLUMN_INSTALLMENT_COUNT = "installmentCount";
        static final String COLUMN_DW_TYPE = "dwType";

        static final String COLUMN_IS_CANCEL = "isCancel";

        static final String COLUMN_CURRENCY = "currency";
        static final String COLUMN_BALANCE = "balance";
        static final String COLUMN_USER_NAME = "userName";
        static final String COLUMN_PRIORITY = "priority";
        static final String COLUMN_USER_PRIORITY = "user_priority";


        static final String COLUMN_SMS_TYPE = "smsType";


        static final String indexing = "CREATE INDEX qlip_reg_idx ON " + RegExpressionTable.TABLE_NAME + " (" + COLUMN_SMS_TYPE + COMMA_SEP + COLUMN_SENDER + COMMA_SEP + COLUMN_PRIORITY + COMMA_SEP + COLUMN_USER_PRIORITY + ")";

        static final String INSERT_REG = "INSERT OR REPLACE INTO" + ParserReaderContract.RegExpressionTable.TABLE_NAME +
                "(" +
                ParserReaderContract.RegExpressionTable.COLUMN_REG_ID + "," +
                ParserReaderContract.RegExpressionTable.COLUMN_SENDER + "," +
                ParserReaderContract.RegExpressionTable.COLUMN_REG_EXPRESSION + "," +
                ParserReaderContract.RegExpressionTable.COLUMN_CARD_NAME + "," +
                ParserReaderContract.RegExpressionTable.COLUMN_CARD_TYPE + "," +
                ParserReaderContract.RegExpressionTable.COLUMN_CARD_SUB_TYPE+ "," +
                ParserReaderContract.RegExpressionTable.COLUMN_CARD_NUM + "," +
                ParserReaderContract.RegExpressionTable.COLUMN_SPENT_MONEY + "," +
                ParserReaderContract.RegExpressionTable.COLUMN_SPENT_DATE + "," +
                ParserReaderContract.RegExpressionTable.COLUMN_KEYWORD + "," +
                ParserReaderContract.RegExpressionTable.COLUMN_INSTALLMENT_COUNT + "," +
                ParserReaderContract.RegExpressionTable.COLUMN_DW_TYPE + "," +
                ParserReaderContract.RegExpressionTable.COLUMN_IS_CANCEL + "," +
                ParserReaderContract.RegExpressionTable.COLUMN_CURRENCY + "," +
                ParserReaderContract.RegExpressionTable.COLUMN_BALANCE + "," +
                ParserReaderContract.RegExpressionTable.COLUMN_USER_NAME + "," +
                ParserReaderContract.RegExpressionTable.COLUMN_SMS_TYPE + "," +
                ParserReaderContract.RegExpressionTable.COLUMN_PRIORITY + ")" +
                "VALUES";

        static final String SQL_CREATE_ENTRIES =
                CREATE_TABLE + TABLE_NAME + " (" +
                        COLUMN_REG_ID + INTEGER_TYPE + PRIMARY_KEY + COMMA_SEP +
                        COLUMN_SENDER + TEXT_TYPE + NOT_NULL + DEFAULT + DEFAULT_TEXT + COMMA_SEP +
                        COLUMN_REG_EXPRESSION + TEXT_TYPE + NOT_NULL + DEFAULT + DEFAULT_TEXT + COMMA_SEP +
                        COLUMN_CARD_NAME + TEXT_TYPE + NOT_NULL + DEFAULT + DEFAULT_TEXT + COMMA_SEP +
                        COLUMN_CARD_TYPE + TEXT_TYPE + NOT_NULL + DEFAULT + DEFAULT_TEXT + COMMA_SEP +
                        COLUMN_CARD_SUB_TYPE + TEXT_TYPE + NOT_NULL + DEFAULT + DEFAULT_TEXT + COMMA_SEP +
                        COLUMN_CARD_NUM + TEXT_TYPE  + COMMA_SEP +
                        COLUMN_SPENT_MONEY + TEXT_TYPE + NOT_NULL + DEFAULT + DEFAULT_TEXT + COMMA_SEP +
                        COLUMN_SPENT_DATE + TEXT_TYPE + COMMA_SEP +
                        COLUMN_KEYWORD + TEXT_TYPE + NOT_NULL + DEFAULT + DEFAULT_TEXT + COMMA_SEP +
                        COLUMN_INSTALLMENT_COUNT + TEXT_TYPE  + COMMA_SEP +
                        COLUMN_DW_TYPE + TEXT_TYPE + NOT_NULL + DEFAULT + DEFAULT_TEXT + COMMA_SEP +
                        COLUMN_IS_CANCEL + TEXT_TYPE + NOT_NULL + DEFAULT + DEFAULT_TEXT + COMMA_SEP +
                        COLUMN_CURRENCY + TEXT_TYPE + COMMA_SEP +
                        COLUMN_BALANCE + TEXT_TYPE + NOT_NULL + DEFAULT + DEFAULT_TEXT + COMMA_SEP +
                        COLUMN_USER_NAME + TEXT_TYPE + COMMA_SEP +
                        COLUMN_PRIORITY + INTEGER_TYPE + NOT_NULL + DEFAULT + DEFAULT_INT + COMMA_SEP +
                        COLUMN_USER_PRIORITY + INTEGER_TYPE + NOT_NULL + DEFAULT + DEFAULT_INT + COMMA_SEP +

                        COLUMN_SMS_TYPE + INTEGER_TYPE + NOT_NULL + DEFAULT + DEFAULT_INT +

                        " )";


        static final String SQL_DELETE_ENTRIES =
                DROP_TABLE_IF_EXISTS + TABLE_NAME;


        static RegData populateModel(Cursor c, Context context) {

            RegData model = new RegData();
            model.regId = c.getInt(c.getColumnIndex(COLUMN_REG_ID));
            model.sender = c.getString(c.getColumnIndex(COLUMN_SENDER));
            model.regExpression = AES128Cipher.getInstance(context).decrypt(c.getString(c.getColumnIndex(COLUMN_REG_EXPRESSION)));
            model.cardName = c.getString(c.getColumnIndex(COLUMN_CARD_NAME));
            model.cardType = c.getString(c.getColumnIndex(COLUMN_CARD_TYPE));
            model.cardSubType = c.getString(c.getColumnIndex(COLUMN_CARD_SUB_TYPE));
            model.cardNum = c.getString(c.getColumnIndex(COLUMN_CARD_NUM));
            model.spentMoney = c.getString(c.getColumnIndex(COLUMN_SPENT_MONEY));
            model.spentDate = c.getString(c.getColumnIndex(COLUMN_SPENT_DATE));
            model.keyword = c.getString(c.getColumnIndex(COLUMN_KEYWORD));
            model.installmentCount = c.getString(c.getColumnIndex(COLUMN_INSTALLMENT_COUNT));
            model.dwType = c.getString(c.getColumnIndex(COLUMN_DW_TYPE));
            model.isCancel = c.getString(c.getColumnIndex(COLUMN_IS_CANCEL));
            model.currency = c.getString(c.getColumnIndex(COLUMN_CURRENCY));
            model.balance = c.getString(c.getColumnIndex(COLUMN_BALANCE));
            model.userName = c.getString(c.getColumnIndex(COLUMN_USER_NAME));
            model.priority = c.getInt(c.getColumnIndex(COLUMN_PRIORITY));
            model.userPriority = c.getInt(c.getColumnIndex(COLUMN_USER_PRIORITY));
            model.smsType = c.getInt(c.getColumnIndex(COLUMN_SMS_TYPE));

            return model;

        }

        static ContentValues populateContent(RegData model) {

            ContentValues values = new ContentValues();
            values.put(COLUMN_REG_ID, model.regId);
            values.put(COLUMN_SENDER, TextUtils.isEmpty(model.sender)?"" :model.sender);
            values.put(COLUMN_REG_EXPRESSION, TextUtils.isEmpty(model.regExpression)?"" : model.regExpression);
            values.put(COLUMN_CARD_NAME, TextUtils.isEmpty(model.cardName)?"":model.cardName);
            values.put(COLUMN_CARD_TYPE, TextUtils.isEmpty(model.cardType)?"":model.cardType);
            values.put(COLUMN_CARD_SUB_TYPE, TextUtils.isEmpty(model.cardSubType)?"":model.cardSubType);
            values.put(COLUMN_CARD_NUM, TextUtils.isEmpty(model.cardNum)?"":model.cardNum);
            values.put(COLUMN_SPENT_MONEY, TextUtils.isEmpty(model.spentMoney)?"":model.spentMoney);
            values.put(COLUMN_SPENT_DATE, TextUtils.isEmpty(model.spentDate)?"":model.spentDate);
            values.put(COLUMN_KEYWORD,  TextUtils.isEmpty(model.cardName)? Constants.KEYWORD_EMPTY:model.keyword);
            values.put(COLUMN_INSTALLMENT_COUNT, TextUtils.isEmpty(model.installmentCount)?"":model.installmentCount);
            values.put(COLUMN_DW_TYPE, TextUtils.isEmpty(model.dwType)?"":model.dwType);
            values.put(COLUMN_IS_CANCEL, TextUtils.isEmpty(model.isCancel)?"":model.isCancel);
            values.put(COLUMN_CURRENCY, TextUtils.isEmpty(model.currency)?"":model.currency);
            values.put(COLUMN_BALANCE, TextUtils.isEmpty(model.balance)?"":model.balance);
            values.put(COLUMN_USER_NAME, TextUtils.isEmpty(model.userName)?"":model.userName);
            values.put(COLUMN_PRIORITY, model.priority);
            values.put(COLUMN_SMS_TYPE, model.smsType);

            return values;
        }
    }

    static class BanksTable implements BaseColumns {
        static final String TABLE_NAME = " BANKS";
        static final String COLUMN_BANK_ID="bankId";
        static final String COLUMN_NAME = "sender";

        static final String SQL_CREATE_ENTRIES =
                CREATE_TABLE + TABLE_NAME + " (" +
                        COLUMN_BANK_ID + INTEGER_TYPE + PRIMARY_KEY + COMMA_SEP +
                        COLUMN_NAME + TEXT_TYPE + NOT_NULL + UNIQUE +
                        " )";

        static final String indexing = "CREATE INDEX qlip_banks_idx ON " + BanksTable.TABLE_NAME + " ("+COLUMN_NAME+")";

        static final String SQL_DELETE_ENTRIES =
                DROP_TABLE_IF_EXISTS + TABLE_NAME;

        static final String INSERT_BANK = "INSERT OR REPLACE INTO" + ParserReaderContract.BanksTable.TABLE_NAME +
                "(" +
                ParserReaderContract.BanksTable.COLUMN_BANK_ID + "," +
                ParserReaderContract.BanksTable.COLUMN_NAME + ")" +
                "VALUES";

        static Bank populateModel(Cursor c) {

            Bank bank = new Bank();
            bank.id = c.getInt(c.getColumnIndex(COLUMN_BANK_ID));
            bank.name = c.getString(c.getColumnIndex(COLUMN_NAME));
            return bank;
        }

        static ContentValues populateContent(Bank model) {

            ContentValues values = new ContentValues();
            values.put(COLUMN_BANK_ID, model.id);
            values.put(COLUMN_NAME, model.name);
            return values;
        }

    }


    static class SendersTable implements BaseColumns {
        static final String TABLE_NAME = " SENDERS";
        static final String COLUMN_SENDER_ID="senderId";
        static final String COLUMN_SMS_TYPE = "smsType";
        static final String COLUMN_SENDER = "sender";
        static final String COLUMN_REP_SENDER = "repSender";

        static final String SQL_CREATE_ENTRIES =
                CREATE_TABLE + TABLE_NAME + " (" +
                        COLUMN_SENDER_ID + INTEGER_TYPE + PRIMARY_KEY + COMMA_SEP +
                        COLUMN_SMS_TYPE + INTEGER_TYPE + NOT_NULL + DEFAULT + DEFAULT_INT+ COMMA_SEP +
                        COLUMN_SENDER + TEXT_TYPE + NOT_NULL + UNIQUE + COMMA_SEP +
                        COLUMN_REP_SENDER + TEXT_TYPE + NOT_NULL + DEFAULT + DEFAULT_TEXT  +
                        " )";

        static final String indexing = "CREATE INDEX qlip_senders_idx ON " + SendersTable.TABLE_NAME + " ("+COLUMN_SENDER+")";

        static final String SQL_DELETE_ENTRIES =
                DROP_TABLE_IF_EXISTS + TABLE_NAME;

        static final String INSERT_SENDER = "INSERT OR REPLACE INTO" + ParserReaderContract.SendersTable.TABLE_NAME +
                "(" +
        ParserReaderContract.SendersTable.COLUMN_SENDER_ID + "," +
        ParserReaderContract.SendersTable.COLUMN_SMS_TYPE + "," +
        ParserReaderContract.SendersTable.COLUMN_SENDER + "," +
        ParserReaderContract.SendersTable.COLUMN_REP_SENDER + ")" +
                "VALUES";

        static Sender populateModel(Cursor c) {

            Sender sender = new Sender();
            sender.senderId = c.getInt(c.getColumnIndex(COLUMN_SENDER_ID));
            sender.smsType = c.getInt(c.getColumnIndex(COLUMN_SMS_TYPE));
            sender.sender = c.getString(c.getColumnIndex(COLUMN_SENDER));
            sender.repSender = c.getString(c.getColumnIndex(COLUMN_REP_SENDER));
            return sender;

        }

        static ContentValues populateContent(Sender model) {

            ContentValues values = new ContentValues();
            values.put(COLUMN_SENDER_ID, model.senderId);
            values.put(COLUMN_SMS_TYPE, model.smsType);
            values.put(COLUMN_SENDER, TextUtils.isEmpty(model.sender)?"":model.sender);
            values.put(COLUMN_REP_SENDER, TextUtils.isEmpty(model.repSender)?"":model.repSender);
            return values;
        }

    }


    static class CardTable implements BaseColumns {


        static final String TABLE_NAME = " CARDS";
        static final String AS_ALIAS = " AS card ";
        static final String ALIAS = " card.";
        static final String COLUMN_CARD_NAME = "cardName";
        static final String COLUMN_CARD_NUM = "cardNum";
        static final String COLUMN_CARD_TYPE = "cardType";
        static final String COLUMN_CARD_SUB_TYPE = "cardSubType";
        static final String COLUMN_CARD_BALANCE = "balance";


        static final String SQL_CREATE_ENTRIES =
                CREATE_TABLE + TABLE_NAME + " (" +
                        _ID + INTEGER_TYPE + PRIMARY_KEY + AUTOINCREMENT + COMMA_SEP +
                        COLUMN_CARD_NAME + TEXT_TYPE + NOT_NULL + DEFAULT + DEFAULT_TEXT + COMMA_SEP +
                        COLUMN_CARD_NUM + TEXT_TYPE  + COMMA_SEP +
                        COLUMN_CARD_TYPE + INTEGER_TYPE + NOT_NULL + DEFAULT + DEFAULT_INT + COMMA_SEP +
                        COLUMN_CARD_SUB_TYPE + INTEGER_TYPE + NOT_NULL + DEFAULT + DEFAULT_INT + COMMA_SEP +
                        COLUMN_CARD_BALANCE + REAL_TYPE + NOT_NULL + DEFAULT + DEFAULT_INT +
                        " )";

        static final String indexing = "CREATE INDEX qlip_card_idx ON " + CardTable.TABLE_NAME + " ("+COLUMN_CARD_NAME + "," +
                COLUMN_CARD_NUM + "," +
                COLUMN_CARD_TYPE + "," +
                COLUMN_CARD_SUB_TYPE + ")";

        static final String INSERT_CARD = "INSERT OR REPLACE INTO" +TABLE_NAME +
                "(" +
                COLUMN_CARD_NAME + "," +
                COLUMN_CARD_NUM + "," +
                COLUMN_CARD_TYPE + "," +
                COLUMN_CARD_SUB_TYPE + "," +
                COLUMN_CARD_BALANCE + ")" +
                "VALUES";

        static final String SQL_DELETE_ENTRIES =
                DROP_TABLE_IF_EXISTS + TABLE_NAME;

        static CardTableData populateModel(Cursor c) {

            CardTableData cardTableData = new CardTableData();

            cardTableData.cardId = c.getInt(c.getColumnIndex(_ID));
            cardTableData.cardName = c.getString(c.getColumnIndex(COLUMN_CARD_NAME));
            cardTableData.cardNum = c.getString(c.getColumnIndex(COLUMN_CARD_NUM));
            cardTableData.cardType = c.getInt(c.getColumnIndex(COLUMN_CARD_TYPE));
            cardTableData.cardSubType = c.getInt(c.getColumnIndex(COLUMN_CARD_SUB_TYPE));
            cardTableData.balance = c.getDouble(c.getColumnIndex(COLUMN_CARD_BALANCE));

            return cardTableData;
        }

        static ContentValues populateContent(CardTableData model) {

            ContentValues values = new ContentValues();
            values.put(COLUMN_CARD_NAME, model.cardName==null?Constants.NONE:model.cardName);
            values.put(COLUMN_CARD_NUM, model.cardNum==null?Constants.NONE:model.cardNum);
            values.put(COLUMN_CARD_TYPE, model.cardType);
            values.put(COLUMN_CARD_SUB_TYPE, model.cardSubType);
            values.put(COLUMN_CARD_BALANCE, model.balance);
            return values;

        }

    }


    static class TransactionsTable implements BaseColumns {


        static final String TABLE_NAME = " TRANSACTIONS";
        static final String ALIAS = " tran.";
        static final String AS_ALIAS = " AS tran ";

        static final String COLUMN_IDENTIFIER = "identifier";

        static final String COLUMN_CARD_ID = "cardId";

        static final String COLUMN_SMS_ID = "smsId";

        static final String COLUMN_FULL_SMS = "fullSms";
        static final String COLUMN_SENDER = "sender";
        static final String COLUMN_SMS_DATE = "smsDate";
        static final String COLUMN_SMS_TYPE = "smsType";
        static final String COLUMN_REG_ID = "regId";

        static final String COLUMN_SPENT_MONEY = "spentMoney";
        static final String COLUMN_SPENT_DATE = "spentDate";
        static final String COLUMN_FINISH_DATE = "finishDate";
        static final String COLUMN_KEYWORD = "keyword";
        static final String COLUMN_INSTALLMENT_COUNT = "installmentCount";
        static final String COLUMN_DW_TYPE = "dwType";
        static final String COLUMN_CURRENCY = "currency";

        static final String COLUMN_IS_OFFSET = "isOffset";
        static final String COLUMN_IS_DUPLICATE = "isDuplicate";
        static final String COLUMN_MEMO = "memo";
        static final String COLUMN_SPENT_LATITUDE = "spentLatitude";
        static final String COLUMN_SPENT_LONGITUDE = "spentLongitude";
        static final String COLUMN_CATEGORY_CODE = "categoryCode";
        static final String COLUMN_IS_SUCCESS = "isSuccess";
        static final String COLUMN_ORIGIN_INFOS = "originInfos";

        static final String INSERT_TRANSACTION = "INSERT OR REPLACE INTO" +TABLE_NAME +
                "(" +
                COLUMN_IDENTIFIER + "," +
                COLUMN_CARD_ID + "," +
                COLUMN_SMS_ID + "," +
                COLUMN_FULL_SMS + "," +
                COLUMN_SENDER + "," +
                COLUMN_SMS_DATE + "," +
                COLUMN_SMS_TYPE + "," +
                COLUMN_REG_ID + "," +
                COLUMN_SPENT_MONEY + "," +
                COLUMN_SPENT_DATE + "," +
                COLUMN_FINISH_DATE + "," +
                COLUMN_KEYWORD + "," +
                COLUMN_INSTALLMENT_COUNT + "," +
                COLUMN_DW_TYPE + "," +
                COLUMN_CURRENCY + "," +
                COLUMN_IS_OFFSET + "," +
                COLUMN_IS_DUPLICATE + "," +
                COLUMN_MEMO + "," +
                COLUMN_SPENT_LATITUDE + "," +
                COLUMN_SPENT_LONGITUDE + "," +
                COLUMN_CATEGORY_CODE + "," +
                COLUMN_IS_SUCCESS + "," +
                COLUMN_ORIGIN_INFOS + ")" +
                "VALUES";


        static final String indexing = "CREATE INDEX qlip_transaction_idx ON " + TransactionsTable.TABLE_NAME + " ("+COLUMN_IDENTIFIER+")";
        static final String indexing_2 = "CREATE INDEX qlip_transaction_idx2 ON " + TransactionsTable.TABLE_NAME + " ("+COLUMN_SPENT_DATE + "," + COLUMN_DW_TYPE + "," + COLUMN_IS_OFFSET + "," + COLUMN_KEYWORD+")";
        static final String indexing_3 = "CREATE INDEX qlip_transaction_idx3 ON " + TransactionsTable.TABLE_NAME + " ("+COLUMN_SPENT_DATE + "," + COLUMN_DW_TYPE + "," + COLUMN_SPENT_MONEY + "," + COLUMN_CATEGORY_CODE+")";

        static final String SQL_CREATE_ENTRIES =

                CREATE_TABLE + TABLE_NAME + " (" +
                        COLUMN_IDENTIFIER + TEXT_TYPE + NOT_NULL + UNIQUE + COMMA_SEP+

                        COLUMN_CARD_ID + INTEGER_TYPE + NOT_NULL + DEFAULT + DEFAULT_INT + COMMA_SEP +
                        COLUMN_SMS_ID + INTEGER_TYPE + NOT_NULL + DEFAULT + DEFAULT_INT + COMMA_SEP +
                        COLUMN_FULL_SMS + TEXT_TYPE + NOT_NULL + DEFAULT + DEFAULT_TEXT + COMMA_SEP +
                        COLUMN_SENDER + TEXT_TYPE + NOT_NULL + DEFAULT + DEFAULT_TEXT + COMMA_SEP +
                        COLUMN_SMS_TYPE + INTEGER_TYPE + NOT_NULL + DEFAULT + DEFAULT_INT+ COMMA_SEP +
                        COLUMN_SMS_DATE + DATE_TYPE + NOT_NULL + DEFAULT + DEFAULT_INT + COMMA_SEP +
                        COLUMN_REG_ID + INTEGER_TYPE + NOT_NULL + DEFAULT + DEFAULT_INT+ COMMA_SEP +

                        COLUMN_SPENT_MONEY + REAL_TYPE + NOT_NULL + DEFAULT + DEFAULT_INT + COMMA_SEP +
                        COLUMN_SPENT_DATE + DATE_TYPE + NOT_NULL + DEFAULT + DEFAULT_INT + COMMA_SEP +
                        COLUMN_FINISH_DATE + DATE_TYPE + NOT_NULL + DEFAULT + DEFAULT_INT + COMMA_SEP +
                        COLUMN_KEYWORD + TEXT_TYPE + NOT_NULL + DEFAULT + DEFAULT_TEXT + COMMA_SEP +
                        COLUMN_INSTALLMENT_COUNT + INTEGER_TYPE + NOT_NULL + DEFAULT + "1" + COMMA_SEP +
                        COLUMN_DW_TYPE + INTEGER_TYPE + NOT_NULL + DEFAULT + DEFAULT_INT + COMMA_SEP +
                        COLUMN_CURRENCY + TEXT_TYPE + COMMA_SEP +
                        COLUMN_IS_OFFSET + INTEGER_TYPE + NOT_NULL + DEFAULT + DEFAULT_INT + COMMA_SEP +
                        COLUMN_IS_DUPLICATE + INTEGER_TYPE + NOT_NULL + DEFAULT + DEFAULT_INT + COMMA_SEP +
                        COLUMN_MEMO + TEXT_TYPE + COMMA_SEP +
                        COLUMN_SPENT_LATITUDE + REAL_TYPE + NOT_NULL + DEFAULT + DEFAULT_INT + COMMA_SEP +
                        COLUMN_SPENT_LONGITUDE + REAL_TYPE + NOT_NULL + DEFAULT + DEFAULT_INT + COMMA_SEP +
                        COLUMN_CATEGORY_CODE + INTEGER_TYPE + NOT_NULL+DEFAULT+"101010" + COMMA_SEP+
                        COLUMN_IS_SUCCESS + INTEGER_TYPE + NOT_NULL + DEFAULT + "1" + COMMA_SEP +
                        COLUMN_ORIGIN_INFOS + TEXT_TYPE + NOT_NULL+DEFAULT + DEFAULT_TEXT+

                        ")";

        static final String SQL_DELETE_ENTRIES =
                DROP_TABLE_IF_EXISTS + TABLE_NAME;



        static TransactionTableData populateModel(Cursor c) {

            TransactionTableData model = new TransactionTableData();

            model.identifier = c.getString(c.getColumnIndex(TransactionsTable.COLUMN_IDENTIFIER));

            model.cardTableData = new CardTableData();
            model.cardTableData.cardId =  c.getInt(c.getColumnIndex(COLUMN_CARD_ID));

            model.smsId = c.getInt(c.getColumnIndex(COLUMN_SMS_ID));
            model.fullSms = c.getString(c.getColumnIndex(COLUMN_FULL_SMS));
            model.sender = c.getString(c.getColumnIndex(COLUMN_SENDER));
            model.smsDate = c.getString(c.getColumnIndex(COLUMN_SMS_DATE));
            model.smsType = c.getInt(c.getColumnIndex(COLUMN_SMS_TYPE));
            model.regId = c.getInt(c.getColumnIndex(COLUMN_REG_ID));

            model.spentMoney = c.getDouble(c.getColumnIndex(TransactionsTable.COLUMN_SPENT_MONEY));
            model.spentDate = c.getString(c.getColumnIndex(TransactionsTable.COLUMN_SPENT_DATE));
            model.finishDate = c.getString(c.getColumnIndex(TransactionsTable.COLUMN_FINISH_DATE));
            model.keyword = c.getString(c.getColumnIndex(TransactionsTable.COLUMN_KEYWORD));
            model.installmentCount = c.getInt(c.getColumnIndex(TransactionsTable.COLUMN_INSTALLMENT_COUNT));
            model.dwType = c.getInt(c.getColumnIndex(TransactionsTable.COLUMN_DW_TYPE));
            model.currency = c.getString(c.getColumnIndex(TransactionsTable.COLUMN_CURRENCY));
            model.isOffset = c.getInt(c.getColumnIndex(TransactionsTable.COLUMN_IS_OFFSET));
            model.isDuplicate = c.getInt(c.getColumnIndex(TransactionsTable.COLUMN_IS_DUPLICATE));
            model.memo = c.getString(c.getColumnIndex(TransactionsTable.COLUMN_MEMO));
            model.spentLatitude = c.getDouble(c.getColumnIndex(TransactionsTable.COLUMN_SPENT_LATITUDE));
            model.spentLongitude = c.getDouble(c.getColumnIndex(TransactionsTable.COLUMN_SPENT_LONGITUDE));
            model.categoryCode = c.getInt(c.getColumnIndex(TransactionsTable.COLUMN_CATEGORY_CODE));
            model.isSuccess = c.getInt(c.getColumnIndex(TransactionsTable.COLUMN_IS_SUCCESS));
            model.originInfos = c.getString(c.getColumnIndex(TransactionsTable.COLUMN_ORIGIN_INFOS));

            return model;
        }

        static ContentValues populateContent(TransactionTableData model) {

            ContentValues values = new ContentValues();

            values.put(TransactionsTable.COLUMN_IDENTIFIER, model.identifier);
            values.put(TransactionsTable.COLUMN_CARD_ID, model.cardTableData.cardId);

            values.put(TransactionsTable.COLUMN_SMS_ID, model.smsId);
            values.put(TransactionsTable.COLUMN_FULL_SMS, model.fullSms == null? Constants.NONE : model.fullSms);
            values.put(TransactionsTable.COLUMN_SENDER, model.sender == null? Constants.NONE : model.sender);
            values.put(TransactionsTable.COLUMN_SMS_DATE, model.smsDate == null? DateUtil.getStringDateAsYYYYMMddHHmmss(Calendar.getInstance()) : model.smsDate);
            values.put(TransactionsTable.COLUMN_SMS_TYPE, model.smsType);
            values.put(TransactionsTable.COLUMN_REG_ID, model.regId);

            values.put(TransactionsTable.COLUMN_SPENT_MONEY, model.spentMoney);
            values.put(TransactionsTable.COLUMN_SPENT_DATE, model.spentDate == null? DateUtil.getStringDateAsYYYYMMddHHmmss(Calendar.getInstance()) : model.spentDate);
            values.put(TransactionsTable.COLUMN_FINISH_DATE, model.finishDate == null? DateUtil.getStringDateAsYYYYMMddHHmmss(Calendar.getInstance()) : model.finishDate);
            values.put(TransactionsTable.COLUMN_KEYWORD, model.keyword == null? Constants.KEYWORD_EMPTY : model.keyword);
            values.put(TransactionsTable.COLUMN_INSTALLMENT_COUNT, model.installmentCount);
            values.put(TransactionsTable.COLUMN_DW_TYPE, model.dwType);
            values.put(TransactionsTable.COLUMN_CURRENCY, model.currency);
            values.put(TransactionsTable.COLUMN_IS_OFFSET, model.isOffset);
            values.put(TransactionsTable.COLUMN_IS_DUPLICATE, model.isDuplicate);
            values.put(TransactionsTable.COLUMN_MEMO, model.memo == null? Constants.NONE : model.memo);
            values.put(TransactionsTable.COLUMN_SPENT_LATITUDE, model.spentLatitude);
            values.put(TransactionsTable.COLUMN_SPENT_LONGITUDE, model.spentLongitude);
            values.put(TransactionsTable.COLUMN_CATEGORY_CODE, model.categoryCode);
            values.put(TransactionsTable.COLUMN_IS_SUCCESS, Constants.YES);
            values.put(TransactionsTable.COLUMN_ORIGIN_INFOS, model.originInfos == null? Constants.NONE : model.originInfos);

            return values;
        }

    }

}

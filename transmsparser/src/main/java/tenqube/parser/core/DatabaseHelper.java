package tenqube.parser.core;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import tenqube.parser.model.Bank;
import tenqube.parser.model.RegData;
import tenqube.parser.model.Sender;

import static tenqube.parser.core.ParserReaderContract.BanksTable.INSERT_BANK;
import static tenqube.parser.core.ParserReaderContract.RegExpressionTable.INSERT_REG;
import static tenqube.parser.core.ParserReaderContract.SendersTable.INSERT_SENDER;
import static tenqube.parser.core.ParserService.mIsDebug;
import static tenqube.parser.util.LogUtil.LOGI;
import static tenqube.parser.util.LogUtil.makeLogTag;

class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper mInstance = null;
    private static final String DATABASE_NAME = ".tenqube.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = makeLogTag(DatabaseHelper.class);
    private Context mContext;

    public static DatabaseHelper getInstance(Context context) {
        synchronized (DatabaseHelper.class) {
            if (mInstance == null) {
                mInstance = new DatabaseHelper(context);
            }
            return mInstance;
        }
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) throws SQLiteException {

        db.execSQL(ParserReaderContract.RegExpressionTable.SQL_DELETE_ENTRIES);
        db.execSQL(ParserReaderContract.RegExpressionTable.SQL_CREATE_ENTRIES);
        db.execSQL(ParserReaderContract.RegExpressionTable.indexing);

        db.execSQL(ParserReaderContract.SendersTable.SQL_DELETE_ENTRIES);
        db.execSQL(ParserReaderContract.SendersTable.SQL_CREATE_ENTRIES);
        db.execSQL(ParserReaderContract.SendersTable.indexing);

        db.execSQL(ParserReaderContract.BanksTable.SQL_DELETE_ENTRIES);
        db.execSQL(ParserReaderContract.BanksTable.SQL_CREATE_ENTRIES);
        db.execSQL(ParserReaderContract.BanksTable.indexing);

        db.execSQL(ParserReaderContract.CardTable.SQL_DELETE_ENTRIES);
        db.execSQL(ParserReaderContract.CardTable.SQL_CREATE_ENTRIES);
        db.execSQL(ParserReaderContract.CardTable.indexing);

        db.execSQL(ParserReaderContract.TransactionsTable.SQL_DELETE_ENTRIES);
        db.execSQL(ParserReaderContract.TransactionsTable.SQL_CREATE_ENTRIES);
        db.execSQL(ParserReaderContract.TransactionsTable.indexing);
        db.execSQL(ParserReaderContract.TransactionsTable.indexing_2);
        db.execSQL(ParserReaderContract.TransactionsTable.indexing_3);

        insertRegDatas(db);
        insertSenders(db);
        insertBanks(db);

    }

    //db import 방식으로 변경할것.
    private void insertRegDatas(SQLiteDatabase db) throws SQLiteException{

        if(db!=null){
            AssetManager am = mContext.getAssets();
            InputStream inStream;
            ArrayList<String> values = new ArrayList<>();
            int i  = 0;

            try {
                inStream = am.open("parsing_rule_encrypt.tsv");

                BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
                String line ;
                while ((line = buffer.readLine()) != null) {
                    if(i == 0){
                        i++;
                        continue;
                    }

                    String[] colums = line.split("\t");
                    RegData regData = new RegData();
                    regData.regId = Integer.parseInt(colums[0]);
                    regData.regExpression = TextUtils.isEmpty(colums[1]) ? "" :colums[1];
                    regData.sender = TextUtils.isEmpty(colums[2]) ? "" :colums[2] ;
                    regData.cardName = TextUtils.isEmpty(colums[3]) ? "" :colums[3];
                    regData.cardNum = TextUtils.isEmpty(colums[4]) ? "" :colums[4];
                    regData.cardType = TextUtils.isEmpty(colums[5]) ? "" :colums[5];
                    regData.cardSubType = TextUtils.isEmpty(colums[6]) ? "" :colums[6];
                    regData.balance = TextUtils.isEmpty(colums[7]) ? "" :colums[7];
                    regData.spentMoney = TextUtils.isEmpty(colums[8]) ? "" :colums[8];
                    regData.spentDate = TextUtils.isEmpty(colums[9]) ? "" :colums[9];
                    regData.keyword = TextUtils.isEmpty(colums[10]) ? "" :colums[10];
                    regData.installmentCount = TextUtils.isEmpty(colums[11]) ? "" :colums[11];
                    regData.dwType = TextUtils.isEmpty(colums[12]) ? "" :colums[12];
                    regData.isCancel = TextUtils.isEmpty(colums[13]) ? "" :colums[13];
                    regData.currency = TextUtils.isEmpty(colums[14]) ? "" :colums[14];
                    regData.userName = TextUtils.isEmpty(colums[15]) ? "" :colums[15];
                    regData.priority = TextUtils.isEmpty(colums[16]) ? 0 : Integer.parseInt(colums[16]);
                    regData.smsType = TextUtils.isEmpty(colums[17]) ? 2 : Integer.parseInt(colums[17]);


                    values.add(regData.getInsertValue());
                    if(values.size()==200) {
                        db.execSQL(INSERT_REG+ TextUtils.join(",", values));
                        values.clear();
                    }

                    i++;
                }
                if(!values.isEmpty()) {
                    db.execSQL(INSERT_REG + TextUtils.join(",", values));
                    values.clear();
                }
                LOGI(TAG, "inserted parsing rule size: " + i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * sender_id;sms_type;sender;rep_sender
     * @param db SQLiteDatabase
     */
    private void insertSenders(SQLiteDatabase db) throws SQLiteException{

        if(db!=null) {
            ArrayList<String> values = new ArrayList<>();
            AssetManager am = mContext.getAssets();
            InputStream inStream;
            int i  = 0;

            try {
                inStream = am.open("senders.tsv");

                BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
                String line ;
                while ((line = buffer.readLine()) != null) {
                    if(i == 0){
                        i++;
                        continue;
                    }

                    String[] colums = line.split("\t");
                    Sender sender = new Sender();
                    sender.senderId = Integer.parseInt(colums[0]);
                    sender.smsType = Integer.parseInt(colums[1]);
                    sender.sender = colums[2];
                    sender.repSender = colums[3];

                    values.add(sender.getInsertValue());

                    i++;
                }

                if(!values.isEmpty()) {
                    String insertQuery = INSERT_SENDER + TextUtils.join(",", values);
                    db.execSQL(insertQuery);
                    values.clear();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void insertBanks(SQLiteDatabase db) throws SQLiteException{

        if(db!=null) {
            ArrayList<String> values = new ArrayList<>();
            AssetManager am = mContext.getAssets();
            InputStream inStream;
            int i  = 0;
            try {
                inStream = am.open("banks.tsv");
                BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
                String line ;
                while ((line = buffer.readLine()) != null) {
                    if(i == 0){
                        i++;
                        continue;
                    }

                    String[] colums = line.split("\t");
                    Bank bank = new Bank();
                    bank.id = Integer.parseInt(colums[0]);
                    bank.name = (colums[1]);
                    values.add(bank.getInsertValue());

                    i++;
                }

                if(!values.isEmpty()) {
                    String insertQuery = INSERT_BANK + TextUtils.join(",", values);
                    db.execSQL(insertQuery);
                    values.clear();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) throws SQLiteException {


    }




}
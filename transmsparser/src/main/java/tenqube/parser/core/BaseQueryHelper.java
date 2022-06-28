package tenqube.parser.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import static tenqube.parser.util.LogUtil.makeLogTag;

class BaseQueryHelper {

    public static final String TAG = makeLogTag(BaseQueryHelper.class);
    static final String SELECT = " SELECT ";
    static final String VALUES = " VALUES ";
    static final String EXISTS = " EXISTS ";
    static final String LIMIT = " LIMIT ";
    static final String ORDER_BY = " ORDER BY ";
    static final String FROM = " FROM ";
    static final String WHERE = " WHERE ";
    static final String GROUP_BY = " GROUP BY ";
    static final String HAVING = " HAVING ";
    static final String JOIN = " JOIN ";
    static final String ON = " ON ";
    static final String AND = " AND ";
    static final String IN = " IN ";
    static final String NOT_IN = " NOT IN ";
    static final String OR = " OR ";
    static final String DESC = " DESC ";
    static final String ASC = " ASC ";
    static final String YYYY_MM_DD_H_M = "'%Y-%m-%d %H:%M'";
    static final String YYYY_MM_DD = "'%Y-%m-%d'";
    static final String MM_DD = "'%m-%d'";
    static final String H_M_S = "'%H:%M:%S'";
    static final String H_M = "'%H:%M'";


    private SQLiteDatabase db;
    SQLiteDatabase wdb;
    Context mContext;

    BaseQueryHelper(Context context) throws SecurityException, SQLException  {
        mContext = context;

        db = DatabaseHelper.getInstance(context).getReadableDatabase();
        wdb = DatabaseHelper.getInstance(context).getWritableDatabase();
    }

    /**
     * Called to insert an item.
     *
     * @param tableName 삽입 되는 테이블명
     * @param values    삽입 되는 값
     * @return return 삽입된 행수
     */
    long insert(String tableName, ContentValues values) throws SecurityException, SQLException {
        return wdb.insert(tableName, null, values);
    }
    /**
     * Called to update an item.
     *
     * @param tableName     업데이트 되는 테이블명
     * @param values        업데이트 되는 값
     * @param selection     where 조건 파라미터
     * @param selectionArgs where 조건 값
     * @return return 업데이트된 행수
     */
    long update(String tableName, ContentValues values, String selection, String[] selectionArgs) throws SecurityException, SQLException {

        return wdb.update(tableName, values, selection, selectionArgs);
    }
    /**
     * Called to delete an item.
     *
     * @param tableName     삭제 되는 테이블명
     * @param selection     where 조건 파라미터
     * @param selectionArgs where 조건 값
     * @return return 삭제된 행수
     */
    long delete(String tableName, String selection, String[] selectionArgs) throws SecurityException, SQLException {
        return wdb.delete(tableName, selection, selectionArgs);
    }

    Cursor runQuery(final String query) throws SecurityException, SQLException {
        Cursor rows;
        rows = db.rawQuery(query, null);

        if (rows == null) {
            return null;
        }

        try {
            final int rowCount = rows.getCount();

            if (rowCount == 0 || !rows.moveToLast()) {
                rows.close();
                return null;
            }
        } catch (RuntimeException ex) {
            rows.close();
            return null;
        }
        return rows;
    }


    String getJoinTable(){
        return ParserReaderContract.TransactionsTable.TABLE_NAME + ParserReaderContract.TransactionsTable.AS_ALIAS +
                JOIN + ParserReaderContract.CardTable.TABLE_NAME + ParserReaderContract.CardTable.AS_ALIAS +
                ON + ParserReaderContract.TransactionsTable.ALIAS + ParserReaderContract.TransactionsTable.COLUMN_CARD_ID +
                "=" + ParserReaderContract.CardTable.ALIAS + ParserReaderContract.CardTable._ID;

    }
}

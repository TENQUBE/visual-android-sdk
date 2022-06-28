package tenqube.parser.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * SharedPreferences Utils
 */

class PrefUtils {

    static final String RULE_VERSION = "ruleVersion";
    static final String TRAN_COUNT = "TRAN_COUNT";
    static final String SECURITY_KEY = "securityKey";
    static final String MAX_SMS_ID = "MAX_SMS_ID";
    static final String MAX_MMS_ID = "MAX_MMS_ID";


    @SuppressLint("StaticFieldLeak")
    private static  PrefUtils mInstance;
    private Context context;

    static PrefUtils getInstance(Context context) {
        synchronized (DatabaseHelper.class) {
            if (mInstance == null) {
                mInstance = new PrefUtils(context);
            }
            return mInstance;
        }
    }

    private PrefUtils(Context context){
        this.context = context;
    }


    int loadIntValue(String key, int defaultValue){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(key, defaultValue);
    }

    void saveIntValue(String key, int value){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putInt(key, value).apply();
    }

    String loadStringValue(String key, String defaultValue){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(key, defaultValue);
    }

    void saveStringValue(String key, String value){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(key, value).apply();
    }

    void clear() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().clear().apply();

    }

}
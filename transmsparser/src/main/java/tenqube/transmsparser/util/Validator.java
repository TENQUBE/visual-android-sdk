package tenqube.transmsparser.util;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import tenqube.transmsparser.constants.Constants;

/**
 * Created by tenqube on 2017. 4. 14..
 */

public class Validator {


    private static Map<String, Integer> VALIDATOR = new HashMap();
    public static final String CARD_NAME = "cardName";
    public static final String CARD_NUM = "cardNum";
    public static final String KEYWORD = "keyword";
    public static final String SENDER = "sender";
    public static final String FULL_SMS = "fullSms";
    public static final String MEMO = "memo";
    public static final SimpleDateFormat fullDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);


    static {
        VALIDATOR.put(CARD_NAME, 50);
        VALIDATOR.put(CARD_NUM, 50);
        VALIDATOR.put(KEYWORD, 50);
        VALIDATOR.put(SENDER, 50);
        VALIDATOR.put(FULL_SMS, 500);
        VALIDATOR.put(MEMO, 300);
    }

    public static boolean invalidStr(String key, String value) {
        Integer strLength = VALIDATOR.get(key);
        if(strLength!=null)
            return !TextUtils.isEmpty(value) && value.length() > strLength;

        return true;

    }


    public static boolean isDate(String dateStr){

        if(dateStr == null){
            return false;
        }

        fullDF.setLenient(false);

        try {

            fullDF.parse(dateStr);

        } catch (Exception e) {
            return false;
        }

        return dateStr.length() == 19;
    }

    private static Pattern numberPattern = Pattern.compile("[0-9]+", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE);
    public static boolean isNumber (String value) {

        return numberPattern.matcher(value).matches();
    }


    public static String getValidString(String value){

        return !TextUtils.isEmpty(value) && value.length() < 50 ? value.trim() : "";

    }

    public static String getValidFullSMS(String fullSMS) {
        return !TextUtils.isEmpty(fullSMS) && fullSMS.length() < 500 ? fullSMS.trim() : "";
    }

    public static int getValidInstallmentCount(int installmentCount) {
        return installmentCount >= 1 && installmentCount <= 36 ? installmentCount : 1;
    }


    private static Pattern currencyPattern = Pattern.compile("[A-Z]{3}", Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE | Pattern.UNICODE_CASE);
    public static String getValidCurrency(String currency) {

        return currencyPattern.matcher(currency).matches()? currency : Constants.NONE;
    }




}


package tenqube.parser.util;

import android.util.Log;

import com.tenqube.shared.util.Utils;

import tenqube.parser.BuildConfig;

/**
 * Created by tenqube on 2017. 4. 10..
 */

public class LogUtil {

    private static final String LOG_PREFIX = "tenqube_";
    private static final int LOG_PREFIX_LENGTH = LOG_PREFIX.length();
    private static final int MAX_LOG_TAG_LENGTH = 23;

    public static String makeLogTag(String str) {
        if (str.length() > MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH) {
            return LOG_PREFIX + str.substring(0, MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH - 1);
        }

        return LOG_PREFIX + str;
    }

    public static String makeLogTag(Class cls) {
        return makeLogTag(cls.getSimpleName());
    }

    public static void LOGI(final String tag, String message) {
        if (Utils.INSTANCE.isDebug()) {
            Log.i(Utils.TAG, tag + "/" + "message: " + message);
        }
    }

    public static void LOGE(final String tag, String message) {
        Log.e(tag, message);
    }


}

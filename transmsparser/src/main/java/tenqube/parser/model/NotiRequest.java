package tenqube.parser.model;

/**
 * Created by tenqube on 2017. 3. 28..
 */

import android.content.Context;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

import tenqube.parser.core.Mapper;


public class NotiRequest implements Serializable {
    private static final SimpleDateFormat fullDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);

    /**
     * 패키지 명
     * sbn.getPackageName()
     */
    private final String pkgName;

    /**
     * notification.extras.getCharSequence(Notification.EXTRA_TITLE)
     */
    private final String title;

    /**
     * notification.extras.getCharSequence(Notification.EXTRA_TEXT);
     */
    private final String text;

    /**
     * notification.extras.getCharSequence(Notification.EXTRA_BIG_TEXT);
     */
    private final String bigText;

    /**
     * notification.when
     */
    private final long when;

    public NotiRequest(@NotNull String pkgName, @NotNull String title, @NotNull String text, @Nullable String bigText, long when) {
        this.pkgName = pkgName;
        this.title = title;
        this.text = text;
        this.bigText = bigText;
        this.when = when;
    }

    public String getPkgName() {
        return pkgName;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getBigText() {
        return bigText;
    }

    public long getWhen() {
        return when;
    }

    public SMS toSMS(@Nullable Context context) {
        return Mapper.toSMS(context, this);
    }

}

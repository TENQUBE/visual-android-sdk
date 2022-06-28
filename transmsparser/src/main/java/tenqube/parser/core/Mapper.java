package tenqube.parser.core;

import android.content.Context;
import android.text.TextUtils;

import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import tenqube.parser.constants.Constants;
import tenqube.parser.model.NotiRequest;
import tenqube.parser.model.SMS;

public class Mapper {

    public static SMS toSMS(@Nullable Context context, NotiRequest notiRequest) {

        SMS sms = makeTestSMS(notiRequest);
        if(sms != null) return sms;

        boolean isDefault = context != null && Utils.isDefaultSMS(context, notiRequest.getPkgName());
        String title = Utils.filterTitle(notiRequest.getTitle());

        SMS newSMS = new SMS(0,
                getFullSms(notiRequest.getPkgName(), notiRequest.getBigText(), notiRequest.getText(), isDefault),
                getSender(isDefault, notiRequest.getPkgName(), title),
                "",
                getSmsDate(notiRequest.getWhen()),
                getSMStype(isDefault),
                title);

        newSMS.setShouldIgnoreDate(isDefault);

        return newSMS;

    }

    /**
     * 행아웃 테스트 url
     * `com.kbcard.kbkookmincard;title;KB국민비씨 이*성님 02/20 20:45 200,000원 단기카드대출 사용 누적 536,844원
     * @param notiRequest
     * @return
     */
    private static SMS makeTestSMS(NotiRequest notiRequest) {

        if("com.google.android.talk".equals(notiRequest.getPkgName())) {
            if(notiRequest.getText().contains("`")) {
                String[] notis = notiRequest.getText().substring(1).split(";");
                String title = "";
                String text = "";
                String sender = "";
                int smsType = Constants.SMSType.NOTIFICATION.ordinal();

                if(notis.length > 0) {
//
                    int i = 0;
                    for(String noti : notis) {
                        if(i == 0) {
                            sender = noti;
                        } else if(i == 1) {
                            title = noti;
                        } else if (i == 2) {
                            text = noti;
                        } else  if (i == 3) {
                            smsType = Integer.parseInt(noti);
                        }
                        i++;
                    }

                    return new SMS(0, text,
                            sender,
                            "",
                            getSmsDate(Calendar.getInstance().getTimeInMillis()),
                            smsType,
                            title);
                }
            }
        }

        return null;
    }

    /**
     * [Web발신] 하나(4*7*) 이*진님 일시불 999원 02/18 11:55 삼성웰스토리(주)
     *  [Web발신] 하나(4*7*) 이*진님 일시불 1,000원 02/18 11:5 4 삼성웰스토리(주)
     *  [Web발신] 하나(4*7*) [Web발신] 하나(4*7*) 이*진님 일시불 1,000원 02/18 11:5 4 삼성웰스토리(주)
     * sms 데이터의 경우 text
     * @param pkg
     * @param bigText
     * @param text
     * @return
     */
    private static String getFullSms(String pkg, String bigText, String text, boolean isDefaultSMS) {

        if(isDefaultSMS) {
            if(bigText == null) bigText = "";
            if(text == null) text = "";

            String tempText = text.length() > bigText.length()? text : bigText;

            String[] texts = tempText.split("\\[[a-zA-Z]{3}발신\\]");
            if(texts.length > 1) {
                return texts[texts.length - 1].trim();
            }
        }

        if("com.hyundaicard.appcard".equals(pkg) && !TextUtils.isEmpty(bigText)) {
            return bigText;
        }

        return text;
    }

    /**
     * 디폴트 문자앱의 패키지인경우 title을 센더로 나머지는 pkgName
     * @return
     */
    private static String getSender(boolean isDefault, String pkgName, String title) {

        if(isDefault) {
            return title;
        } else {
            return pkgName;
        }

    }


    private static String getSmsDate(long when) {
        SimpleDateFormat fullDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        return fullDF.format(new Date(when));
    }

    private static int getSMStype(boolean isDefault) {
        if(isDefault) {
            return Constants.SMSType.SMS.ordinal();
        } else {
            return Constants.SMSType.NOTIFICATION.ordinal();
        }
    }


}

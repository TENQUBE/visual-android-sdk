package tenqube.transmsparser.model;

/**
 * Created by tenqube on 2017. 3. 28..
 */

import java.io.Serializable;

import tenqube.transmsparser.util.Validator;


public class SMS implements Serializable {


    public SMS(int smsId, String fullSms, String sender, String displaySender, String smsDate, int smsType) {
        this(smsId, fullSms, sender, displaySender, smsDate, smsType, "");
    }

    public SMS(int smsId, String fullSms, String sender, String displaySender, String smsDate, int smsType, String title) {
        this.smsId = smsId;
        this.fullSms = fullSms == null? "": fullSms.trim();
        this.sender = sender;
        this.displaySender = displaySender;
        this.smsDate = smsDate;
        this.smsType = smsType;
        this.title = title;
    }

    /**
     * SMSSMS{smsId=647, fullSms='[Web발신]
     현대ZERO승인 이*우
     38,700원 일시불
     03/23 17:16 (주)신세계백화점
     누적74,396원
     0.7% 할인', displaySender='15776200', sender='15776200', smsDate='2018-03-28 18:03', smsType=1, title='null'}
     * @return
     */
    public boolean isValid () {
        return fullSms != null &&
                fullSms.length() < 500 &&
                sender != null &&
                smsDate != null &&
                Validator.isDate(smsDate);
    }

    /**
     * 문자 아이디
     */
    private final int smsId;

    /**
     * 문자 메세지
     */
    private final String fullSms;

    private final String displaySender;
    /**
     * 전화번호
     */
    private final String sender;

    /**
     * 수신 날짜 (YYYY-MM-dd HH:mm:ss)
     */
    private final String smsDate;

    /**
     * 0:NOTIFICATION 1:MMS 2: SMS
     */
    private final int smsType;

    private final String title;

    private boolean shouldIgnoreDate;

    public boolean shouldIgnoreDate() {
        return shouldIgnoreDate;
    }

    public void setShouldIgnoreDate(boolean shouldIgnoreDate) {
        this.shouldIgnoreDate = shouldIgnoreDate;
    }

    @Override
    public String toString() {
        return "SMS{" +
                "smsId=" + smsId +
                ", fullSms='" + fullSms + '\'' +
                ", displaySender='" + displaySender + '\'' +
                ", sender='" + sender + '\'' +
                ", smsDate='" + smsDate + '\'' +
                ", smsType=" + smsType +
                '}';
    }


    public int getSmsId() {
        return smsId;
    }

    public String getFullSms() {
        return fullSms;
    }

    public String getDisplaySender() {
        return displaySender;
    }

    public String getSender() {
        return sender;
    }

    public String getSmsDate() {
        return smsDate;
    }

    public int getSmsType() {
        return smsType;
    }

    public String getTitle() {
        return title;
    }

}

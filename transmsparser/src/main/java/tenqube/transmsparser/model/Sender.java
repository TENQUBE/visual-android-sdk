package tenqube.transmsparser.model;

import java.io.Serializable;

/**
 * Created by tenqube on 2017. 3. 27..
 */

public class Sender implements Serializable {


    public Sender() {
    }


    /**
     * PK
     */
    public int senderId;

    /**
     * 문자 타입
     *  0:notification 1:mms 2:sms
     */
    public int smsType;

    /**
     * 수신 번호
     */
    public String sender;


    /**
     * 대표 송신자
     */
    public String repSender;


    /**
     * 삭제여부
     *
     * 0:삭제 아님 1:삭제
     */
    public int isDelete;

    public String getInsertValue() {
        return "(" +
                senderId + "," +
                smsType + "," +
                "'" + sender + "'," +
                "'" + repSender + "')" ;
    }

    @Override
    public String toString() {
        return "Sender{" +
                "senderId=" + senderId +
                ", smsType=" + smsType +
                ", sender='" + sender + '\'' +
                ", repSender='" + repSender + '\'' +
                ", isDelete=" + isDelete +
                '}';
    }
}

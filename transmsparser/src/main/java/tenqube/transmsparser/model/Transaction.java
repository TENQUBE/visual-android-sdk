package tenqube.transmsparser.model;

import java.io.Serializable;

/**
 * Created by tenqube on 2017. 3. 27..
 */

public class Transaction implements Serializable{

    public Transaction() {
    }


    @Override
    public String toString() {
        return "Transaction{" +
                "identifier='" + identifier + '\'' +
                ", cardName='" + cardName + '\'' +
                ", cardNum='" + cardNum + '\'' +
                ", cardType=" + cardType +
                ", cardSubType=" + cardSubType +
                ", spentMoney=" + spentMoney +
                ", spentDate='" + spentDate + '\'' +
                ", finishDate='" + finishDate + '\'' +
                ", keyword='" + keyword + '\'' +
                ", installmentCount=" + installmentCount +
                ", dwType=" + dwType +
                ", currency='" + currency + '\'' +
                ", balance=" + balance +
                ", smsId=" + smsId +
                ", sender='" + sender + '\'' +
                ", regId=" + regId +
                ", fullSms='" + fullSms + '\'' +
                ", smsDate='" + smsDate + '\'' +
                ", smsType=" + smsType +
                ", isOffset=" + isOffset +
                ", isDuplicate=" + isDuplicate +
                ", memo='" + memo + '\'' +
                ", spentLatitude=" + spentLatitude +
                ", spentLongitude=" + spentLongitude +
                ", locationAccuracy=" + locationAccuracy +
                ", categoryCode=" + categoryCode +
                ", isSuccess=" + isSuccess +
                ", isCurrentTran=" + isCurrentTran +
                '}';
    }

    /**
     * 클라이언트 키값
     */
    public String identifier;

    /**
     * 카드 or 은행사 이름
     */
    public String cardName;

    /**
     * 카드 or 은행 번호
     */
    public String cardNum;

    /**
     * 0:체크 1:신용 2:계좌
     */
    public int cardType;

    /**
     * 0:일반 1:법인 2:가족
     */
    public int cardSubType;


    /**
     * 사용금액
     */
    public double spentMoney;

    /**
     * 사용날짜
     */
    public String spentDate;

    /**
     * 할부 마지막 날짜
     */
    public String finishDate;

    /**
     * 키워드
     */
    public String keyword;

    /**
     * 할부 개월수
     */
    public int installmentCount;

    /**
     * 0:수입 1:지출
     */
    public int dwType;

    /**
     * 통화
     */
    public String currency;

    /**
     * 잔액
     */
    public double balance;

    public Double parsedBalance;

    public int smsId;
    /**
     * 카드사 or 은행사 전화번호
     */
    public String sender;


    /**
     * 매칭된 regId
     */
    public int regId;
    /**
     * 문자 메세지
     */
    public String fullSms;

    /**
     * 문자 날짜
     */
    public String smsDate;

    /**
     * 문자 타입
     * 0:notification 1:mms 2:sms
     */
    public int smsType;


    /**
     * 승인 - 취소 상쇄 여부
     * 0: 상쇄 안됨 1: 상쇄됨
     */
    public int isOffset;

    /**
     * 중복 여부
     * 0: 중복아님 1: 중복
     */
    public int isDuplicate;

    /**
     * 메모
     */
    public String memo;

    /**
     * 위도
     */
    public double spentLatitude;

    /**
     * 경도
     */
    public double spentLongitude;

    /**
     * 위치 정확도
     */
    public double locationAccuracy;


    /**
     * 카테고리 코드
     */
    public int categoryCode;

    /**
     * 서버 성공 여부
     */
    public int isSuccess;

    /**
     * 실시간 내역 여부
     */
    public boolean isCurrentTran;

}

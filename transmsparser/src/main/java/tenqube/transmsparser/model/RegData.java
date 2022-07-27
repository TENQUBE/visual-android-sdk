package tenqube.transmsparser.model;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by tenqube on 2017. 3. 27..
 */

public class RegData implements Serializable {




    /**
     * PK
     */
    public int regId;

    /**
     * 카드사 or 은행사 전화번호
     */
    public String sender;

    /**
     * 정규 표현식
     */
    public String regExpression;

    /**
     * 카드 or 은행사 이름
     */
    public String cardName;

    /**
     * 0:체크 1:신용 2:계좌
     */
    public String cardType;

    /**
     * 0:일반 1:법인 2:가족
     */
    public String cardSubType;

    /**
     * 카드번호 or 계좌번호
     */
    public String cardNum;

    /**
     * 사용금액
     */
    public String spentMoney;

    /**
     * 사용날짜
     */
    public String spentDate;

    /**
     * 키워드
     */
    public String keyword;

    /**
     * 할부 개월수
     */
    public String installmentCount;

    /**
     * 0:수입 1:지출
     */
    public String dwType;

    /**
     * 취소여부 0:취소 아님 1:취소
     */
    public String isCancel;

    /**
     * 통화
     */
    public String currency;

    /**
     * 잔액
     */
    public String balance;


    /**
     * 사용자 이름
     * 개인정보 보호를 위해 이름을 qlip님으로 변경
     */
    public String userName;


    /**
     * 문자 타입
     *  0:notification 1:mms 2:sms
     */
    public int smsType;

    /**
     * 삭제여부
     *  0:삭제 아님 1:삭제
     */
    public int isDelete;

    /**
     * 표현식 우선순위
     *
     * 우선순위가 높은 순서로 order by함
     *
     */
    public int priority;


    /**
     *  사용빈도에 따른 우선순위 변경 값 (내부 사용)
     */
    public int userPriority;

    public String getInsertValue() {
        return "(" +
                regId + "," +
                "'" + (TextUtils.isEmpty(sender) ? "" :sender) + "'," +
                "'" + (TextUtils.isEmpty(regExpression) ? "" :regExpression) + "'," +
                "'" + (TextUtils.isEmpty(cardName) ? "" :cardName) + "'," +
                "'" + (TextUtils.isEmpty(cardType) ? "" :cardType) + "'," +
                "'" + (TextUtils.isEmpty(cardSubType) ? "" :cardSubType) + "'," +
                "'" + (TextUtils.isEmpty(cardNum) ? "" :cardNum) + "'," +
                "'" + (TextUtils.isEmpty(spentMoney) ? "" :spentMoney) + "'," +
                "'" + (TextUtils.isEmpty(spentDate) ? "" :spentDate) + "'," +
                "'" + (TextUtils.isEmpty(keyword) ? "" :keyword) + "'," +
                "'" + (TextUtils.isEmpty(installmentCount) ? "" :installmentCount) + "'," +
                "'" + (TextUtils.isEmpty(dwType) ? "" :dwType) + "'," +
                "'" + (TextUtils.isEmpty(isCancel) ? "" :isCancel) + "'," +
                "'" + (TextUtils.isEmpty(currency) ? "" :currency) + "'," +
                "'" + (TextUtils.isEmpty(balance) ? "" :balance) + "'," +
                "'" + (TextUtils.isEmpty(userName) ? "" :userName) + "'," +
                smsType + "," +
                priority +
                ")" ;
    }
    @Override
    public String toString() {
        return "RegData{" +
                "regId=" + regId +
                ", sender='" + sender + '\'' +
                ", regExpression='" + regExpression + '\'' +
                ", cardName='" + cardName + '\'' +
                ", cardType='" + cardType + '\'' +
                ", cardSubType='" + cardSubType + '\'' +
                ", cardNum='" + cardNum + '\'' +
                ", spentMoney='" + spentMoney + '\'' +
                ", spentDate='" + spentDate + '\'' +
                ", keyword='" + keyword + '\'' +
                ", installmentCount='" + installmentCount + '\'' +
                ", dwType='" + dwType + '\'' +
                ", isCancel='" + isCancel + '\'' +
                ", currency='" + currency + '\'' +
                ", balance='" + balance + '\'' +
                ", userName='" + userName + '\'' +
                ", smsType=" + smsType +
                ", isDelete=" + isDelete +
                ", priority=" + priority +
                ", userPriority=" + userPriority +
                '}';
    }
}

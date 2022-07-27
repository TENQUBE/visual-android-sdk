package tenqube.transmsparser.core;


class TransactionTableData{

    public TransactionTableData() {

    }

    public String insertValue() {
        return "(" +
                "'"+identifier + "'," +
                cardTableData.cardId + "," +
                smsId + "," +
                "'" + fullSms + "'," +
                "'" + sender + "'," +
                "'" + smsDate + "'," +
                "'" + smsType + "'," +
                regId + "," +
                spentMoney + "," +
                "'" + spentDate + "'," +
                "'" + finishDate + "'," +
                "'" + keyword + "'," +
                installmentCount + "," +
                dwType + "," +
                "'" + currency + "'," +
                isOffset + "," +
                isDuplicate + "," +
                "'" + memo + "'," +
                spentLatitude + "," +
                spentLongitude + "," +
                categoryCode + "," +
                isSuccess + "," +
                "'" + originInfos + "'" +
                ")" ;
    }

    @Override
    public String toString() {
        return "TransactionTableData{" +
                "identifier='" + identifier + '\'' +
                ", cardTableData=" + cardTableData +
                ", originInfos='" + originInfos + '\'' +
                ", parsedBalance=" + parsedBalance +
                ", sender='" + sender + '\'' +
                ", smsId='" + smsId + '\'' +
                ", fullSms='" + fullSms + '\'' +
                ", smsDate='" + smsDate + '\'' +
                ", smsType=" + smsType +
                ", spentMoney=" + spentMoney +
                ", spentDate='" + spentDate + '\'' +
                ", finishDate='" + finishDate + '\'' +
                ", keyword='" + keyword + '\'' +
                ", installmentCount=" + installmentCount +
                ", dwType=" + dwType +
                ", currency='" + currency + '\'' +
                ", isOffset=" + isOffset +
                ", isDuplicate=" + isDuplicate +
                ", memo='" + memo + '\'' +
                ", spentLatitude=" + spentLatitude +
                ", spentLongitude=" + spentLongitude +
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
     * 카드 정보
     */
    public CardTableData cardTableData;


    /**
     * 중복 처리를 위한 초기값 저장
     */
    public String originInfos;

    /**
     * 파싱된 잔액
     */
    public Double parsedBalance = null;


    /**
     * 카드사 or 은행사 전화번호
     */
    public String sender;



    /**
     * 문자 아이디
     */
    public int smsId;

    /**
     * 매칭된 regId
     */
    public int regId;

    public int userPriority;
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

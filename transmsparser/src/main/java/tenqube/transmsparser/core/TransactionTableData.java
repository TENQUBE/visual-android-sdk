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
     * ??????????????? ??????
     */
    public String identifier;

    /**
     * ?????? ??????
     */
    public CardTableData cardTableData;


    /**
     * ?????? ????????? ?????? ????????? ??????
     */
    public String originInfos;

    /**
     * ????????? ??????
     */
    public Double parsedBalance = null;


    /**
     * ????????? or ????????? ????????????
     */
    public String sender;



    /**
     * ?????? ?????????
     */
    public int smsId;

    /**
     * ????????? regId
     */
    public int regId;

    public int userPriority;
    /**
     * ?????? ?????????
     */
    public String fullSms;

    /**
     * ?????? ??????
     */
    public String smsDate;

    /**
     * ?????? ??????
     * 0:notification 1:mms 2:sms
     */
    public int smsType;

    /**
     * ????????????
     */
    public double spentMoney;

    /**
     * ????????????
     */
    public String spentDate;

    /**
     * ?????? ????????? ??????
     */
    public String finishDate;

    /**
     * ?????????
     */
    public String keyword;

    /**
     * ?????? ?????????
     */
    public int installmentCount;

    /**
     * 0:?????? 1:??????
     */
    public int dwType;

    /**
     * ??????
     */
    public String currency;

    /**
     * ?????? - ?????? ?????? ??????
     * 0: ?????? ?????? 1: ?????????
     */
    public int isOffset;

    /**
     * ?????? ??????
     * 0: ???????????? 1: ??????
     */
    public int isDuplicate;

    /**
     * ??????
     */
    public String memo;

    /**
     * ??????
     */
    public double spentLatitude;

    /**
     * ??????
     */
    public double spentLongitude;


    /**
     * ???????????? ??????
     */
    public int categoryCode;

    /**
     * ?????? ?????? ??????
     */
    public int isSuccess;

    /**
     * ????????? ?????? ??????
     */
    public boolean isCurrentTran;

}

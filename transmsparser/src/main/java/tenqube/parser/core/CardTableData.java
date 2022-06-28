package tenqube.parser.core;

/**
 * Created by tenqube on 2017. 4. 12..
 */

class CardTableData {

    CardTableData() {}

    int cardId;

    String cardName;

    String cardNum;

    int cardType;

    int cardSubType;

    double balance;


    @Override
    public String toString() {
        return "CardTableData{" +
                "cardId=" + cardId +
                ", cardName='" + cardName + '\'' +
                ", cardNum='" + cardNum + '\'' +
                ", cardType=" + cardType +
                ", cardSubType=" + cardSubType +
                ", balance=" + balance +
                '}';
    }
}

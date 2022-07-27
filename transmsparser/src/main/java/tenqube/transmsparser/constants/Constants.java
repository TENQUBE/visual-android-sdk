package tenqube.transmsparser.constants;



public class Constants {

    public static final int YES = 1;
    public static final int NO = 0;
    public static final String SAMSUNG_PAY = "삼성페이";
    public static final String LG_PAY = "LG Pay";

    public static final String NONE = "none";
    public static final String KEYWORD_EMPTY = "내용없음";
    public static final String ALL = "ALL";

    public enum SMSType{
        NOTIFICATION,
        MMS,
        SMS
    }

    public enum DWType{
        DEPOSIT,
        WITHDRAW
    }

    public enum CardType{
        CHECK,
        CARD,
        BANK_ACCOUNT,
        CASH
    }

    public enum CardSubType{
        NORMAL,
        CORPORATION,
        FAMILY
    }

}

package tenqube.parser.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tenqube on 2017. 3. 27..
 */

public class ParsingRule implements Serializable{


    public ParsingRule(String securityKey,
                       int tranCount,
                       int ruleVersion,
                       ArrayList<RegData> regDatas,
                       ArrayList<Sender> senders,
                       ArrayList<Bank> banks,
                       ArrayList<FinancialProductRule> financialProductRules) {
        this.securityKey = securityKey;
        this.tranCount = tranCount;
        this.ruleVersion = ruleVersion;
        this.regDatas = regDatas;
        this.senders = senders;
        this.banks = banks;
        this.financialProductRules = financialProductRules;
    }

    /**
     * 암호화 키
     */
    public String securityKey;

    /**
     * 벌크 업로드시 서버로 보낼 수
     */
    public int tranCount;

    /**
     * 파싱 룰 최대 버전
     */
    public int ruleVersion;


    /**
     * 파싱 룰 정보
     */
    public ArrayList<RegData> regDatas = new ArrayList<>();


    /**
     * 파싱 가능한 전화번호 리스트 {@link Sender}
     */
    public ArrayList<Sender> senders = new ArrayList<>();

    /**
     * 카카오톡 에서 사용할 은행목록
     */
    public ArrayList<Bank> banks = new ArrayList<>();

    public ArrayList<FinancialProductRule> financialProductRules = new ArrayList<>();

    @Override
    public String toString() {
        return "ParsingRule{" +
                "securityKey='" + securityKey + '\'' +
                ", tranCount=" + tranCount +
                ", ruleVersion=" + ruleVersion +
                ", regDatas=" + regDatas +
                ", senders=" + senders +
                ", banks=" + banks +
                '}';
    }
}

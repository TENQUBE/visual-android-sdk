package tenqube.parser.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tenqube on 2017. 3. 27..
 */

public class ParserResult implements Serializable {


    public ParserResult(int resultCode, ArrayList<Transaction> transactions, FinancialProduct financialProduct) {

        this.resultCode = resultCode;
        this.transactions = transactions;
        this.financialProduct = financialProduct;

    }

    public ParserResult() {

    }

    /**
     * 결과 코드 {@link ResultCode}
     */
    public int resultCode;


    /**
     * 파싱된 내역 리스트 {@link Transaction}
     */
    public ArrayList<Transaction> transactions;

    public FinancialProduct financialProduct;

    @Override
    public String toString() {
        return "ParseResult{" +
                "resultCode=" + resultCode +
                ", transactions=" + transactions +
                '}';
    }
}

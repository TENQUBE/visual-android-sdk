package tenqube.parser;


import java.util.ArrayList;

import tenqube.parser.model.FinancialProduct;
import tenqube.parser.model.ResultCode;
import tenqube.parser.model.SMS;
import tenqube.parser.model.Transaction;

/**
 * Created by tenqube on 2017. 3. 31..
 */

public interface BulkSmsAdapter {

    /**
     * SMS 의 문자함의 수 구하기
     *
     * @return 총 문자함의 수 리턴
     */
    int getSmsCount();

    /**
     * SMS 불러오는 함수
     * 
     * @param n 번째 문자 함수 불러오기
     * @return {@link SMS} 
     */
    SMS getSmsAt(int n);

    /**
     * 현재상태 값에 대한 progress 상태
     *
     * @param now 현재 파싱된 수
     * @param total 문자 전체 수
     */
    void onProgress(int now, int total);

    /**
     * tranCount 단위로 서버 전송 요청
     *
     * @param transactions 파싱된 리스트
     * @param callback 네트워크 성공 여부 callback
     */
    void sendToServerTransactions(ArrayList<Transaction> transactions,
                                  ArrayList<FinancialProduct> products, OnNetworkResultListener callback);

    /**
     * 파싱 완료시 요청
     */
    void onCompleted();

    /**
     * 에러 발생시 요청
     * @param resultCode {@link ResultCode}
     */
    void onError(int resultCode);


}

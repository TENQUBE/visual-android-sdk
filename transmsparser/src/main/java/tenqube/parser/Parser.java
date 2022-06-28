package tenqube.parser;

import android.database.SQLException;

import java.util.ArrayList;

import tenqube.parser.model.ParserResult;
import tenqube.parser.model.ParsingRule;
import tenqube.parser.model.SMS;
import tenqube.parser.model.Transaction;

/**
 * <h1>Parser</h1>
 * @author  SA KWANG JIN
 * @version 1.0
 * @since   2017-04-03
 */

public interface Parser {

    /**
     *
     * @return 현재 Client 룰 버전값을 리턴합니다.
     */
    int getRuleVersion();

    /**
     * 파싱룰 동기화 함수.
     *
     * @param parsingRule 서버에서 전송받은 파싱 규칙
     * @throws SQLException SQLException 발생
     */
    void syncParsingRule(ParsingRule parsingRule) throws SQLException;

    /**
     * 단건
     * 파싱하는 함수이며 ParseResult 값을 리턴 합니다.
     *
     * @param sms {@link SMS} 문자 내역
     * @return {@link ParserResult} 파싱된 내역 리턴
     * @throws SQLException Parsing Rule 동기화 or DB 생성시 발생
     */
    ParserResult parse(SMS sms) throws SQLException;

    /**
     * 단건 네트워크 전송 결과
     *
     * @param transactions 파싱된 내역 리스트
     * @param isSuccess 성공여부
     */
    void onNetworkResult(ArrayList<Transaction> transactions, boolean isSuccess);

    /**
     * 벌크
     * 파싱하는 함수입니다.
     *
     * @param bulkSmsAdapter {@link BulkSmsAdapter}  벌크처리를 위한 adapter
     * @throws SQLException SQLException 발생
     */
    void parseBulk(BulkSmsAdapter bulkSmsAdapter) throws SQLException;


    /**
     *  instance 제거
     *
     */
    void destroy();
    /**
     * 로그 모드 활성화여부를 설정합니다.
     *
     * @param isDebug 로그확인 여부 (default false)
     */
    void setDebugMode(boolean isDebug);

    /**
     * 로컬디비를 초기화 상태로 만드는 함수입니다
     * @throws SQLException DB 에러 발생시
     */
    void initDb() throws SQLException;

    /**
     * 파싱에 필요한 번호를 추가합니다.
     *
     * @param number 추가 하고자하는 번호
     * @throws SQLException 번호 추가시 DB 에러 발생시
     * */
    void addTestOriginNumber(String number) throws SQLException;


    /**
     * 사용자 강제 종료시 호출
     */
    void cancelParseBulk();

    String getRepSender(SMS sender);



}

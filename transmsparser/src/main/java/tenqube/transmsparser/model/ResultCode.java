package tenqube.transmsparser.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tenqube on 2017. 3. 27..
 */

public class ResultCode {

    /**
     * 단건 파싱되지 않음
     */
    public static final int NOT_PARSED = 0;

    /**
     * 단건 파싱된 내역 서버 전송
     */
    public static final int SEND_TO_SERVER = 1;


    /**
     * 파라미터가 올바르지 않은경우
     */
    public static final int PARAMETER_ERROR = -1;

    /**
     * 서버 에러 발생한 경우
     */
    public static final int SERVER_ERROR = -2;

    /**
     * 사용자 강제 종료
     */
    public static final int FORCE_STOP = -3;

    /**
     * 파싱룰 동기화 필요함
     */
    public static final int NEED_TO_SYNC_PARSING_RULE = -4;



    /**
     * 파싱되지 않은 내역 서버로 전송이 필요함
     */
    public static final int NEED_TO_SEND_TO_SERVER = -5;

    /**
     * 파싱룰 동기화 필요함
     */
    public static final int NEED_TO_SYNC_PARSING_RULE_NO_SENDER  = -6;




    private static Map<Integer, String> sErrorCode = new HashMap();

    private ResultCode() {
        throw new AssertionError();
    }

    public static boolean contains(Integer value) {
        return null != value && sErrorCode.containsKey(value);
    }

    public static String stringValueOf(Integer value) {
        return null != value && contains(value)?sErrorCode.get(value):"";
    }

    static {
        sErrorCode.put(0, "Not parsed");
        sErrorCode.put(1, "Send to server");
        sErrorCode.put(-1, "Verify input parameter");
        sErrorCode.put(-2, "Server Error");
        sErrorCode.put(-3, "Force Stop");
        sErrorCode.put(-4, "Need To Sync Parsing Rule");

    }


}

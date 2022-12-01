package stable.horseCafe.web.common.util;

import lombok.experimental.UtilityClass;
import stable.horseCafe.web.common.exception.GlobalException;
import stable.horseCafe.web.common.response.CommonResponse;
import stable.horseCafe.web.common.response.ListResponse;
import stable.horseCafe.web.common.response.SingleResponse;
import stable.horseCafe.web.common.response.code.ResponseCode;

@UtilityClass
public class ResponseUtil {

    public static <T> SingleResponse<T> getSingleResult(String message, T result) {
        return new SingleResponse<>(ResponseCode.OK, message, result);
    }

    public static <T> ListResponse<T> getListResult(String message, T resultList) {
        return new ListResponse<>(ResponseCode.OK, message, resultList);
    }

    public static CommonResponse failResponse(GlobalException ge) {
        return new CommonResponse(ge.getCode(), ge.getMessage());
    }

    public static CommonResponse failResponse(ResponseCode code, String message) {
        return new CommonResponse(code, message);
    }
}

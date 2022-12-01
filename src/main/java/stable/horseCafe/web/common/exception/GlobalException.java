package stable.horseCafe.web.common.exception;


import lombok.Getter;
import stable.horseCafe.web.common.response.code.ResponseCode;

@Getter
public class GlobalException extends RuntimeException {

    ResponseCode code;
    String message;

    public GlobalException(ResponseCode code, String message) {
        this.code = code;
        this.message = message;
    }
}

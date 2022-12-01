package stable.horseCafe.web.common.response;

import lombok.Getter;
import stable.horseCafe.web.common.response.code.ResponseCode;

@Getter
public class CommonResponse {

    int status;
    String message;

    public CommonResponse(ResponseCode code, String message) {
        this.status = code.getStatus();
        this.message = message;
    }
}

package stable.horseCafe.web.common.response;

import lombok.Getter;
import stable.horseCafe.web.common.response.code.ResponseCode;

@Getter
public class SingleResponse<T> extends CommonResponse {

    T data;

    public SingleResponse(ResponseCode code, String message, T data) {
        super(code, message);
        this.data = data;
    }
}

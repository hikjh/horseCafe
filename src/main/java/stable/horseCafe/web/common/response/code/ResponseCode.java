package stable.horseCafe.web.common.response.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {

    OK(200),
    CREATE(200),
    BAD_REQUEST(400),
    FORBIDDEN(403),
    UNPROCESSABLE_ENTITY(422),
    SERVER_ERROR(500);

    private final int status;
}

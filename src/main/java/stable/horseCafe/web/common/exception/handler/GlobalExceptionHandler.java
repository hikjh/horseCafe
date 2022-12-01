package stable.horseCafe.web.common.exception.handler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import stable.horseCafe.web.common.exception.GlobalException;
import stable.horseCafe.web.common.response.CommonResponse;
import stable.horseCafe.web.common.util.ResponseUtil;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public CommonResponse handle(GlobalException ge) {
        return ResponseUtil.failResponse(ge);
    }
}

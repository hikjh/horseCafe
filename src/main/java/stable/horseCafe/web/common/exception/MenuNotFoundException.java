package stable.horseCafe.web.common.exception;

import static stable.horseCafe.web.common.response.code.ResponseCode.BAD_REQUEST;

public class MenuNotFoundException extends GlobalException{

    public MenuNotFoundException() {
        super(BAD_REQUEST, "존재하지 않는 메뉴입니다.");
    }
}

package stable.horseCafe.web.common.exception;

import static stable.horseCafe.web.common.response.code.ResponseCode.NOT_FOUND;

public class MenuNotFoundException extends GlobalException{

    public MenuNotFoundException() {
        super(NOT_FOUND, "존재하지 않는 메뉴입니다.");
    }
}

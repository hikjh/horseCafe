package stable.horseCafe.web.common.exception;

import static stable.horseCafe.web.common.response.code.ResponseCode.BAD_REQUEST;

public class OrderNotFoundException extends GlobalException{

    public OrderNotFoundException() {
        super(BAD_REQUEST, "존재하지 않는 주문입니다.");
    }
}

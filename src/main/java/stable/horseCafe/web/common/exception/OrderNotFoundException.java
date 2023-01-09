package stable.horseCafe.web.common.exception;

import static stable.horseCafe.web.common.response.code.ResponseCode.NOT_FOUND;

public class OrderNotFoundException extends GlobalException{

    public OrderNotFoundException() {
        super(NOT_FOUND, "존재하지 않는 주문입니다.");
    }
}

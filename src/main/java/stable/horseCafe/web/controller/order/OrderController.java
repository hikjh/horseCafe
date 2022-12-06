package stable.horseCafe.web.controller.order;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import stable.horseCafe.config.security.LoginMember;
import stable.horseCafe.domain.member.Member;
import stable.horseCafe.service.order.OrderService;
import stable.horseCafe.web.common.response.CommonResponse;
import stable.horseCafe.web.common.util.ResponseUtil;
import stable.horseCafe.web.dto.order.OrderSaveReqDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/stable/v1/order")
    public CommonResponse order(@LoginMember Member member, @RequestBody List<OrderSaveReqDto> dtoList) {
        return ResponseUtil.getSingleResult("주문", orderService.order(member, dtoList));
    }
}

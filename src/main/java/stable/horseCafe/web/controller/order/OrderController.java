package stable.horseCafe.web.controller.order;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import stable.horseCafe.config.security.LoginMember;
import stable.horseCafe.domain.member.Member;
import stable.horseCafe.service.order.OrderService;
import stable.horseCafe.web.common.response.CommonResponse;
import stable.horseCafe.web.common.util.ResponseUtil;
import stable.horseCafe.web.dto.order.OrderSaveReqDto;
import stable.horseCafe.web.dto.order.OrderSearchCondition;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     *  주문
     */
    @PostMapping("/stable/v1/order")
    public CommonResponse order(@LoginMember Member member, @RequestBody @Valid List<OrderSaveReqDto> dtoList) {
        return ResponseUtil.getSingleResult("주문", orderService.order(member, dtoList));
    }

    /**
     *  주문 취소
     */
    @GetMapping("/stable/v1/cancelOrder/{orderId}")
    public CommonResponse cancelOrder(@LoginMember Member member, @PathVariable Long orderId) {
        return ResponseUtil.getSingleResult("주문 취소", orderService.cancelOrder(member, orderId));
    }

    /**
     *  내 주문 목록 조회
     */
    @GetMapping("/stable/v1/orderList")
    public CommonResponse getMyOrderList(@LoginMember Member member, OrderSearchCondition cond) {
        return ResponseUtil.getSingleResult("내 주문 목록 조회", orderService.getMyOrderList(member, cond));
    }
}

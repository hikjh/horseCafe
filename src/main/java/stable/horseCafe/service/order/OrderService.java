package stable.horseCafe.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stable.horseCafe.domain.member.Member;
import stable.horseCafe.domain.menu.Menu;
import stable.horseCafe.domain.menu.MenuRepository;
import stable.horseCafe.domain.order.Order;
import stable.horseCafe.domain.order.OrderRepository;
import stable.horseCafe.domain.orderMenu.OrderMenu;
import stable.horseCafe.domain.orderMenu.OrderMenuRepository;
import stable.horseCafe.web.common.exception.GlobalException;
import stable.horseCafe.web.common.response.code.ResponseCode;
import stable.horseCafe.web.dto.order.OrderResDto;
import stable.horseCafe.web.dto.order.OrderSaveReqDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final OrderMenuRepository orderMenuRepository;

    /**
     *  주문
     */
    @Transactional
    public Long order(Member member, List<OrderSaveReqDto> dtoList) {

        List<Long> menuIds = dtoList.stream()
                .map(OrderSaveReqDto::getMenuId)
                .collect(Collectors.toList());

        List<Menu> menuList = menuRepository.findAllById(menuIds);

        List<OrderMenu> orderMenuList = new ArrayList<>();
        for (Menu menu : menuList) {
            for (OrderSaveReqDto dto : dtoList) {
                if (menu.getId().equals(dto.getMenuId())) {
                    OrderMenu orderMenu = new OrderMenu(menu, dto.getCount(), menu.getPrice());
                    orderMenuList.add(orderMenu);
                }
            }
        }

        Order order = new Order(member, orderMenuList);
        return orderRepository.save(order).getId();
    }

    /**
     *  주문취소
     */
    @Transactional
    public Long cancelOrder(Long orderId) {
        Order order = orderRepository.findFetchById(orderId)
                .orElseThrow(() -> {
                    throw new GlobalException(ResponseCode.BAD_REQUEST, "존재하지 않는 주문입니다.");
                });

        order.cancel();
        return orderId;
    }

    /**
     *  내 주문 목록 조회
     */
    public List<OrderResDto> getOrderList(Member member) {
        return orderRepository.findOrderList(member.getEmail());
    }
}

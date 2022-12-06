package stable.horseCafe.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stable.horseCafe.domain.member.Member;
import stable.horseCafe.domain.member.MemberRepository;
import stable.horseCafe.domain.menu.Menu;
import stable.horseCafe.domain.menu.MenuRepository;
import stable.horseCafe.domain.order.Order;
import stable.horseCafe.domain.order.OrderRepository;
import stable.horseCafe.domain.orderMenu.OrderMenu;
import stable.horseCafe.web.common.exception.GlobalException;
import stable.horseCafe.web.common.response.code.ResponseCode;
import stable.horseCafe.web.dto.order.OrderSaveReqDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final MenuRepository menuRepository;

    @Transactional
    public Long order(Member member, List<OrderSaveReqDto> dtoList) {

        // 멤버 엔티티 조회
        memberRepository.findByEmail(member.getEmail())
                .orElseThrow(() -> {
                    throw new GlobalException(ResponseCode.BAD_REQUEST, "로그인 후 사용가능합니다.");
                });

        List<Long> menuIds = dtoList.stream()
                .map(OrderSaveReqDto::getMenuId)
                .collect(Collectors.toList());

        List<Menu> menuList = menuRepository.findAllById(menuIds);

        /*
            insert N+1 발생
            - 주문 저장 1회
            - 주문메뉴 저장 2회
         */
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
}

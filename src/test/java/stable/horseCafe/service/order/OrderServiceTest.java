package stable.horseCafe.service.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import stable.horseCafe.domain.member.Member;
import stable.horseCafe.domain.member.MemberRepository;
import stable.horseCafe.domain.menu.Menu;
import stable.horseCafe.domain.menu.MenuRepository;
import stable.horseCafe.domain.order.Order;
import stable.horseCafe.domain.order.OrderRepository;
import stable.horseCafe.web.dto.order.OrderResDto;
import stable.horseCafe.web.dto.order.OrderSaveReqDto;
import stable.horseCafe.web.dto.order.OrderSearchCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static stable.horseCafe.domain.menu.MenuStatus.HOT;
import static stable.horseCafe.domain.menu.MenuType.COFFEE;
import static stable.horseCafe.domain.order.OrderStatus.CANCEL;
import static stable.horseCafe.domain.order.OrderStatus.ORDER;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void clean() {
        orderRepository.deleteAll();
    }

    @BeforeEach
    void signUp() {
        Member member = Member.builder()
                .name("홍길동")
                .email("hong@gmail.com")
                .password("1234")
                .build();
        memberRepository.save(member);
    }

    @BeforeEach
    void registerMenus() {
        List<Menu> menuList = IntStream.range(1, 11)
                .mapToObj(i -> Menu.builder()
                        .name("아메리카노 " + i)
                        .price(4500)
                        .stockQuantity(i * 30)
                        .menuType(COFFEE)
                        .menuStatus(HOT)
                        .build())
                .collect(Collectors.toList());

        menuRepository.saveAll(menuList);
    }

    @Test
    @DisplayName("주문")
    void order() {
        // given
        Member member = memberRepository.findByEmail("hong@gmail.com").get();
        List<Menu> menuList = menuRepository.findAll();

        List<OrderSaveReqDto> dtoList = new ArrayList<>();

        OrderSaveReqDto dto1 = getOrderSaveReqDto(menuList.get(0).getId(), 3);
        OrderSaveReqDto dto2 = getOrderSaveReqDto(menuList.get(1).getId(), 6);
        OrderSaveReqDto dto3 = getOrderSaveReqDto(menuList.get(2).getId(), 9);

        dtoList.add(dto1);
        dtoList.add(dto2);
        dtoList.add(dto3);

        // when
        Long orderId = orderService.order(member, dtoList);
        Order order = orderRepository.findById(orderId).get();

        // then
        assertNotNull(orderId);
        assertEquals("홍길동", order.getMember().getName());
        assertEquals("아메리카노 1", order.getOrderMenus().get(0).getMenu().getName());
        assertEquals("아메리카노 2", order.getOrderMenus().get(1).getMenu().getName());
        assertEquals("아메리카노 3", order.getOrderMenus().get(2).getMenu().getName());
        assertEquals(3, order.getOrderMenus().get(0).getCount());
        assertEquals(6, order.getOrderMenus().get(1).getCount());
        assertEquals(9, order.getOrderMenus().get(2).getCount());
        assertEquals(27, order.getOrderMenus().get(0).getMenu().getStockQuantity());
        assertEquals(54, order.getOrderMenus().get(1).getMenu().getStockQuantity());
        assertEquals(81, order.getOrderMenus().get(2).getMenu().getStockQuantity());
    }

    @Test
    @DisplayName("주문 취소")
    void cancelOrder() {
        /**
         *  메뉴 10개 등록
         *      - 첫번째 : 아메리카노 1, 재고수량 30
         *      - 두번째 : 아메리카노 2, 재고수량 60
         *      - 세번째 : 아메리카노 3, 재고수량 90 ...
         */
        // given
        order();
        Order order = orderRepository.findAll().get(0);

        // when
        orderService.cancelOrder(order.getId());

        // then
        assertEquals(CANCEL, order.getOrderStatus());
        assertEquals(30, order.getOrderMenus().get(0).getMenu().getStockQuantity());
        assertEquals(60, order.getOrderMenus().get(1).getMenu().getStockQuantity());
        assertEquals(90, order.getOrderMenus().get(2).getMenu().getStockQuantity());
    }

    @Test
    @DisplayName("내 주문 목록 조회")
    void getMyOrderList() {
        // given
        Member member = memberRepository.findByEmail("hong@gmail.com").get();
        List<Menu> menuList = menuRepository.findAll();

        List<OrderSaveReqDto> dtoList = new ArrayList<>();
        OrderSaveReqDto dto1 = getOrderSaveReqDto(menuList.get(0).getId(), 1);
        dtoList.add(dto1);
        orderService.order(member, dtoList);

        OrderSaveReqDto dto2 = getOrderSaveReqDto(menuList.get(1).getId(), 2);
        dtoList.add(dto2);
        orderService.order(member, dtoList);

        OrderSearchCondition cond = OrderSearchCondition
                .builder()
                .orderStatus(ORDER)
                .build();

        // when
        List<OrderResDto> orderList = orderService.getMyOrderList(member, cond);

        // then
        assertEquals(2L, orderList.size());
        assertEquals("홍길동", orderList.get(0).getMemberName());
        assertEquals("아메리카노 1", orderList.get(0).getOrderMenus().get(0).getName());
        assertEquals("아메리카노 1", orderList.get(1).getOrderMenus().get(0).getName());
        assertEquals("아메리카노 2", orderList.get(1).getOrderMenus().get(1).getName());
    }

    private OrderSaveReqDto getOrderSaveReqDto(Long id, int count) {
        return OrderSaveReqDto.builder()
                .menuId(id)
                .count(count)
                .build();
    }
}
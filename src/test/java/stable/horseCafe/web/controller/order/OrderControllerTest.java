package stable.horseCafe.web.controller.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import stable.horseCafe.domain.member.Member;
import stable.horseCafe.domain.member.MemberRepository;
import stable.horseCafe.domain.menu.Menu;
import stable.horseCafe.domain.menu.MenuRepository;
import stable.horseCafe.domain.order.Order;
import stable.horseCafe.domain.order.OrderRepository;
import stable.horseCafe.web.dto.order.OrderSaveReqDto;
import stable.horseCafe.web.dto.order.OrderSearchCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.context.support.TestExecutionEvent.TEST_EXECUTION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static stable.horseCafe.domain.menu.MenuStatus.HOT;
import static stable.horseCafe.domain.menu.MenuType.COFFEE;
import static stable.horseCafe.domain.order.OrderStatus.ORDER;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MenuRepository menuRepository;

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
    void registerMenu() {
        List<Menu> menuList = IntStream.range(1, 6)
                .mapToObj(i -> Menu.builder()
                        .name("아메리카노 " + i)
                        .price(5000)
                        .stockQuantity(50)
                        .menuType(COFFEE)
                        .menuStatus(HOT)
                        .build())
                .collect(Collectors.toList());

        menuRepository.saveAll(menuList);
    }

    @Test
    @WithUserDetails(value = "hong@gmail.com", setupBefore = TEST_EXECUTION)
    @DisplayName("주문")
    void order() throws Exception {
        // given
        List<Menu> menuList = menuRepository.findAll();

        List<OrderSaveReqDto> dtoList = new ArrayList<>();
        OrderSaveReqDto dto = OrderSaveReqDto.builder()
                .menuId(menuList.get(0).getId())
                .count(5)
                .build();
        dtoList.add(dto);

        String json = objectMapper.writeValueAsString(dtoList);

        // expected
        mockMvc.perform(post("/stable/v1/order")
                .contentType(APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$.message").value("주문"))
                .andExpect(jsonPath("$.status").value(200))
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = "hong@gmail.com", setupBefore = TEST_EXECUTION)
    @DisplayName("주문 취소")
    void cancelOder() throws Exception {
        // given
        order();
        List<Order> orderList = orderRepository.findAll();

        // expected
        mockMvc.perform(get("/stable/v1/cancelOrder/{orderId}", orderList.get(0).getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("주문 취소"));
    }

    @Test
    @WithUserDetails(value = "hong@gmail.com", setupBefore = TEST_EXECUTION)
    @DisplayName("내 주문 목록 검색 조회")
    void getMyOrderList() throws Exception {
        // given
        order();
        OrderSearchCondition cond = OrderSearchCondition.builder()
                .orderStatus(ORDER).build();

        // expected
        mockMvc.perform(get("/stable/v1/orderList")
                        .contentType(APPLICATION_JSON)
                        .param("orderStatus", cond.getOrderStatus().toString()))
                .andExpect(jsonPath("$.message").value("내 주문 목록 조회"))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data[0].memberName").value("홍길동"))
                .andExpect(jsonPath("$.data[0].orderMenus[0].name").value("아메리카노 1"))
                .andExpect(jsonPath("$.data[0].orderMenus[0].count").value(5))
                .andExpect(jsonPath("$.data[0].totalPrice").value(25000))
                .andDo(print());
    }
}
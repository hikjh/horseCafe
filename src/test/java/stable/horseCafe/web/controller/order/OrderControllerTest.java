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
import stable.horseCafe.domain.orderMenu.OrderMenu;
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
                .name("?????????")
                .email("hong@gmail.com")
                .password("1234")
                .build();
        memberRepository.save(member);
    }

    @BeforeEach
    void registerMenu() {
        List<Menu> menuList = IntStream.range(1, 6)
                .mapToObj(i -> Menu.builder()
                        .name("??????????????? " + i)
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
    @DisplayName("??????")
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
                .andExpect(jsonPath("$.message").value("??????"))
                .andExpect(jsonPath("$.status").value(200))
                .andDo(print());
    }

    @Test
    @WithUserDetails(value = "hong@gmail.com", setupBefore = TEST_EXECUTION)
    @DisplayName("?????? ??????")
    void cancelOder() throws Exception {
        // given
        Order order = saveOrder();

        // expected
        mockMvc.perform(get("/stable/v1/cancelOrder/{orderId}", order.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("?????? ??????"));
    }

    @Test
    @WithUserDetails(value = "hong@gmail.com", setupBefore = TEST_EXECUTION)
    @DisplayName("??? ?????? ?????? ?????? ??????")
    void getMyOrderList() throws Exception {
        // given
        saveOrder();
        OrderSearchCondition cond = OrderSearchCondition.builder()
                .orderStatus(ORDER).build();

        // expected
        mockMvc.perform(get("/stable/v1/orderList")
                        .contentType(APPLICATION_JSON)
                        .param("orderStatus", cond.getOrderStatus().toString()))
                .andExpect(jsonPath("$.message").value("??? ?????? ?????? ??????"))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data[0].memberName").value("?????????"))
                /*
                .andExpect(jsonPath("$.data[0].orderMenus[0].name").value("??????????????? 1"))
                 */
                .andExpect(jsonPath("$.data[0].orderMenus[0].count").value(1))
                .andExpect(jsonPath("$.data[0].totalPrice").value(5000))
                .andDo(print());
    }

    private Order saveOrder() {
        Member member = memberRepository.findAll().get(0);  // ??????
        Menu menu = menuRepository.findAll().get(0);        // ??????

        List<OrderMenu> orderMenuList = new ArrayList<>();  // ?????? ??????
        OrderMenu orderMenu = OrderMenu.builder()
                .menu(menu)
                .count(1)
                .orderMenuPrice(menu.getPrice())
                .build();
        orderMenuList.add(orderMenu);

        Order order = Order.builder()                       // ??????
                .orderMenus(orderMenuList)
                .member(member)
                .build();
        orderRepository.save(order);
        return order;
    }
}
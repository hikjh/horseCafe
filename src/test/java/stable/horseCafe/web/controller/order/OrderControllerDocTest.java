package stable.horseCafe.web.controller.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.context.support.TestExecutionEvent.TEST_EXECUTION;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static stable.horseCafe.domain.menu.MenuStatus.HOT;
import static stable.horseCafe.domain.menu.MenuType.COFFEE;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "stable.horseCafe.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
@Transactional
public class OrderControllerDocTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void signUp() {
        memberRepository.deleteAll();
        Member member = Member.builder()
                .name("?????????")
                .email("hong@gmail.com")
                .password("1234")
                .build();
        memberRepository.save(member);
    }

    @BeforeEach
    void saveMenuList() {
        List<Menu> menuList = IntStream.range(1, 4)
                .mapToObj(i ->
                        Menu.builder()
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
        List<OrderSaveReqDto> dtoList = new ArrayList<>();
        List<Menu> menuList = menuRepository.findAll();

        for (int i = 0; i < menuList.size(); i++) {
            OrderSaveReqDto dto = OrderSaveReqDto.builder()
                    .menuId(menuList.get(i).getId())
                    .count(i + 1)
                    .build();

            dtoList.add(dto);
        }

        String json = objectMapper.writeValueAsString(dtoList);

        // expected
        mockMvc.perform(post("/stable/v1/order")
                .contentType(APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$.message").value("??????"))
                .andDo(print())
                .andDo(document("order-register",
                        requestFields(
                                fieldWithPath("[0].menuId").description("?????? ?????????"),
                                fieldWithPath("[0].count").description("?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("status").description("?????? ??????"),
                                fieldWithPath("message").description("?????? ?????????"),
                                fieldWithPath("data").description("?????? ?????????")
                        )
                ));
    }

    @Test
    @WithUserDetails(value = "hong@gmail.com", setupBefore = TEST_EXECUTION)
    @DisplayName("?????? ??????")
    void cancelOder() throws Exception {
        // given
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

        // expected
        mockMvc.perform(get("/stable/v1/cancelOrder/{orderId}", order.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("?????? ??????"))
                .andDo(print())
                .andDo(document("order-cancel",
                        pathParameters(
                                parameterWithName("orderId").description("?????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("status").description("?????? ??????"),
                                fieldWithPath("message").description("?????? ?????????"),
                                fieldWithPath("data").description("?????? ??????")
                        )
                ));
    }
/*
    @Test
    @WithUserDetails(value = "hong@gmail.com", setupBefore = TEST_EXECUTION)
    @DisplayName("??? ?????? ?????? ?????? ??????")
    void getMyOrderList() {

    }
     */
}

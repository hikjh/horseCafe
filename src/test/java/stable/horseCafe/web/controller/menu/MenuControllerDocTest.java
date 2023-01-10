package stable.horseCafe.web.controller.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import stable.horseCafe.domain.menu.Menu;
import stable.horseCafe.domain.menu.MenuRepository;
import stable.horseCafe.web.dto.menu.MenuSaveReqDto;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static stable.horseCafe.domain.menu.MenuStatus.HOT;
import static stable.horseCafe.domain.menu.MenuType.COFFEE;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "stable.horseCafe.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class MenuControllerDocTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MenuRepository menuRepository;


    @Test
    @WithMockUser(roles = "MEMBER")
    @DisplayName("메뉴 등록")
    void registerMenu() throws Exception {
        // given
        MenuSaveReqDto dto = MenuSaveReqDto.builder()
                .name("아메리카노")
                .price(5000)
                .stockQuantity(20)
                .menuType(COFFEE)
                .menuStatus(HOT)
                .build();

        String json = objectMapper.writeValueAsString(dto);
        // expected
        this.mockMvc.perform(post("/stable/v1/menu")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("menu-register",
                        requestFields(
                                fieldWithPath("name").description("메뉴명"),
                                fieldWithPath("price").description("메뉴 가격"),
                                fieldWithPath("stockQuantity").description("재고 수량"),
                                fieldWithPath("menuType").description("메뉴 유형 - COFFEE, ADE, TEA"),
                                fieldWithPath("menuStatus").description("메뉴 상태 - ICE, HOT")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 코드"),
                                fieldWithPath("message").description("응답 메세지"),
                                fieldWithPath("data").description("메뉴 아이디")
                        )
                ));
    }
    @Test
    @WithMockUser(roles = "MEMBER")
    @DisplayName("메뉴 단건 조회")
    void getMenu() throws Exception {
        // given
        Menu menu = Menu.builder()
                .name("아메리카노")
                .price(5000)
                .stockQuantity(20)
                .menuType(COFFEE)
                .menuStatus(HOT)
                .build();
        menuRepository.save(menu);

        // expected
        this.mockMvc.perform(get("/stable/v1/menu/{menuId}", menu.getId())
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("menu-get",
                        pathParameters(
                                parameterWithName("menuId").description("메뉴 아이디")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 코드"),
                                fieldWithPath("message").description("응답 메세지"),
                                fieldWithPath("data.menuId").description("메뉴 아이디"),
                                fieldWithPath("data.name").description("메뉴명"),
                                fieldWithPath("data.price").description("메뉴 가격"),
                                fieldWithPath("data.stockQuantity").description("재고 수량"),
                                fieldWithPath("data.menuType").description("메뉴 유형 - COFFEE, ADE, TEA"),
                                fieldWithPath("data.menuStatus").description("메뉴 상태 - ICE, HOT")
                        )
                ));
    }
}

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
import stable.horseCafe.domain.menu.MenuStatus;
import stable.horseCafe.web.dto.menu.MenuSaveReqDto;
import stable.horseCafe.web.dto.menu.MenuSearchCondition;
import stable.horseCafe.web.dto.menu.MenuUpdateReqDto;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static stable.horseCafe.domain.menu.MenuStatus.HOT;
import static stable.horseCafe.domain.menu.MenuStatus.ICE;
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
                .andExpect(jsonPath("$.message").value("메뉴 등록"))
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
    @DisplayName("메뉴 수정 - null일 경우 기존 데이터 그대로 유지")
    void editMenu() throws Exception {
        // given
        Menu menu = menuSave();
        MenuUpdateReqDto reqDto = MenuUpdateReqDto.builder()
                .name("아이스 아메리카노")
                .price(4800)
                .menuType(COFFEE)
                .menuStatus(ICE)
                .build();

        String json = objectMapper.writeValueAsString(reqDto);

        // expected
        mockMvc.perform(put("/stable/v1/menu/{menuId}", menu.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.message").value("메뉴 수정"))
                .andDo(print())
                .andDo(document("menu-edit",
                        pathParameters(
                                parameterWithName("menuId").description("메뉴 아이디")
                        ),
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
    @DisplayName("메뉴 삭제")
    void deleteMenu() throws Exception {
        // given
        Menu menu = menuSave();

        // expected
        mockMvc.perform(delete("/stable/v1/menu/{menuId}", menu.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("메뉴 삭제"))
                .andDo(print())
                .andDo(document("menu-delete",
                        pathParameters(
                                parameterWithName("menuId").description("메뉴 아이디")
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
        Menu menu = menuSave();

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

    @Test
    @WithMockUser(roles = "MEMBER")
    @DisplayName("메뉴 목록 조회")
    void getList() throws Exception {
        // given
        saveMenuList();

        // 검색조건 or Page
        MenuSearchCondition cond = MenuSearchCondition.builder()
                .menuStatus(HOT)
                .page(2)
                .build();

        // expected
        mockMvc.perform(get("/stable/v1/menuList")
                        .contentType(APPLICATION_JSON)
                        .param("menuStatus", cond.getMenuStatus().toString())
                        .param("page", cond.getPage().toString()))
                .andExpect(jsonPath("$.message").value("메뉴 목록 검색"))
                .andDo(print())
                .andDo(document("menu-getList",
                        requestParameters(
                                parameterWithName("menuStatus").description("메뉴 상태 - ICE, HOT"),
                                parameterWithName("page").description("페이지")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 코드"),
                                fieldWithPath("message").description("응답 메세지"),
                                fieldWithPath("data[0].menuId").description("메뉴 아이디"),
                                fieldWithPath("data[0].name").description("메뉴명"),
                                fieldWithPath("data[0].price").description("메뉴 가격"),
                                fieldWithPath("data[0].stockQuantity").description("재고 수량"),
                                fieldWithPath("data[0].menuType").description("메뉴 유형 - COFFEE, ADE, TEA"),
                                fieldWithPath("data[0].menuStatus").description("메뉴 상태 - ICE, HOT")
                        )
                ));
    }

    Menu menuSave() {
        Menu menu = Menu.builder()
                .name("아이스 아메리카노")
                .price(4800)
                .stockQuantity(50)
                .menuType(COFFEE)
                .menuStatus(ICE)
                .build();

        menuRepository.save(menu);
        return menu;
    }

    void saveMenuList() {
        List<Menu> menuList = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {

            String name = "아메리카노";
            MenuStatus menuStatus = HOT;
            
            if (i % 2 == 0) {
                name = "아이스 아메리카노";
                menuStatus = ICE;
            }

            Menu menu = Menu.builder()
                    .name(name)
                    .price(4500)
                    .stockQuantity(50)
                    .menuType(COFFEE)
                    .menuStatus(menuStatus)
                    .build();
            menuList.add(menu);
        }
        menuRepository.saveAll(menuList);
    }
}

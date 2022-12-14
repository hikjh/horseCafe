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
import org.springframework.transaction.annotation.Transactional;
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
@Transactional
public class MenuControllerDocTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MenuRepository menuRepository;

    @Test
    @WithMockUser(roles = "MEMBER")
    @DisplayName("?????? ??????")
    void registerMenu() throws Exception {
        // given
        MenuSaveReqDto dto = MenuSaveReqDto.builder()
                .name("???????????????")
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
                .andExpect(jsonPath("$.message").value("?????? ??????"))
                .andDo(print())
                .andDo(document("menu-register",
                        requestFields(
                                fieldWithPath("name").description("?????????"),
                                fieldWithPath("price").description("?????? ??????"),
                                fieldWithPath("stockQuantity").description("?????? ??????"),
                                fieldWithPath("menuType").description("?????? ?????? - COFFEE, ADE, TEA"),
                                fieldWithPath("menuStatus").description("?????? ?????? - ICE, HOT")
                        ),
                        responseFields(
                                fieldWithPath("status").description("?????? ??????"),
                                fieldWithPath("message").description("?????? ?????????"),
                                fieldWithPath("data").description("?????? ?????????")
                        )
                ));
    }

    @Test
    @WithMockUser(roles = "MEMBER")
    @DisplayName("?????? ?????? - null??? ?????? ?????? ????????? ????????? ??????")
    void editMenu() throws Exception {
        // given
        Menu menu = menuSave();
        MenuUpdateReqDto reqDto = MenuUpdateReqDto.builder()
                .name("????????? ???????????????")
                .price(4800)
                .menuType(COFFEE)
                .menuStatus(ICE)
                .build();

        String json = objectMapper.writeValueAsString(reqDto);

        // expected
        mockMvc.perform(put("/stable/v1/menu/{menuId}", menu.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.message").value("?????? ??????"))
                .andDo(print())
                .andDo(document("menu-edit",
                        pathParameters(
                                parameterWithName("menuId").description("?????? ?????????")
                        ),
                        requestFields(
                                fieldWithPath("name").description("?????????"),
                                fieldWithPath("price").description("?????? ??????"),
                                fieldWithPath("stockQuantity").description("?????? ??????"),
                                fieldWithPath("menuType").description("?????? ?????? - COFFEE, ADE, TEA"),
                                fieldWithPath("menuStatus").description("?????? ?????? - ICE, HOT")
                        ),
                        responseFields(
                                fieldWithPath("status").description("?????? ??????"),
                                fieldWithPath("message").description("?????? ?????????"),
                                fieldWithPath("data").description("?????? ?????????")
                        )
                ));
    }

    @Test
    @WithMockUser(roles = "MEMBER")
    @DisplayName("?????? ??????")
    void deleteMenu() throws Exception {
        // given
        Menu menu = menuSave();

        // expected
        mockMvc.perform(delete("/stable/v1/menu/{menuId}", menu.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("?????? ??????"))
                .andDo(print())
                .andDo(document("menu-delete",
                        pathParameters(
                                parameterWithName("menuId").description("?????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("status").description("?????? ??????"),
                                fieldWithPath("message").description("?????? ?????????"),
                                fieldWithPath("data").description("?????? ?????????")
                        )
                ));
    }

    @Test
    @WithMockUser(roles = "MEMBER")
    @DisplayName("?????? ?????? ??????")
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
                                parameterWithName("menuId").description("?????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("status").description("?????? ??????"),
                                fieldWithPath("message").description("?????? ?????????"),
                                fieldWithPath("data.menuId").description("?????? ?????????"),
                                fieldWithPath("data.name").description("?????????"),
                                fieldWithPath("data.price").description("?????? ??????"),
                                fieldWithPath("data.stockQuantity").description("?????? ??????"),
                                fieldWithPath("data.menuType").description("?????? ?????? - COFFEE, ADE, TEA"),
                                fieldWithPath("data.menuStatus").description("?????? ?????? - ICE, HOT")
                        )
                ));
    }

    @Test
    @WithMockUser(roles = "MEMBER")
    @DisplayName("?????? ?????? ??????")
    void getList() throws Exception {
        // given
        saveMenuList();

        // ???????????? or Page
        MenuSearchCondition cond = MenuSearchCondition.builder()
                .menuStatus(HOT)
                .page(2)
                .build();

        // expected
        mockMvc.perform(get("/stable/v1/menuList")
                        .contentType(APPLICATION_JSON)
                        .param("menuStatus", cond.getMenuStatus().toString())
                        .param("page", cond.getPage().toString()))
                .andExpect(jsonPath("$.message").value("?????? ?????? ??????"))
                .andDo(print())
                .andDo(document("menu-getList",
                        requestParameters(
                                parameterWithName("menuStatus").description("?????? ?????? - ICE, HOT"),
                                parameterWithName("page").description("?????????")
                        ),
                        responseFields(
                                fieldWithPath("status").description("?????? ??????"),
                                fieldWithPath("message").description("?????? ?????????"),
                                fieldWithPath("data[0].menuId").description("?????? ?????????"),
                                fieldWithPath("data[0].name").description("?????????"),
                                fieldWithPath("data[0].price").description("?????? ??????"),
                                fieldWithPath("data[0].stockQuantity").description("?????? ??????"),
                                fieldWithPath("data[0].menuType").description("?????? ?????? - COFFEE, ADE, TEA"),
                                fieldWithPath("data[0].menuStatus").description("?????? ?????? - ICE, HOT")
                        )
                ));
    }

    Menu menuSave() {
        Menu menu = Menu.builder()
                .name("????????? ???????????????")
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

            String name = "???????????????";
            MenuStatus menuStatus = HOT;
            
            if (i % 2 == 0) {
                name = "????????? ???????????????";
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

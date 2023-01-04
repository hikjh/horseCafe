package stable.horseCafe.web.controller.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import stable.horseCafe.domain.menu.Menu;
import stable.horseCafe.domain.menu.MenuRepository;
import stable.horseCafe.web.dto.menu.MenuSaveReqDto;
import stable.horseCafe.web.dto.menu.MenuSearchCondition;
import stable.horseCafe.web.dto.menu.MenuUpdateReqDto;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static stable.horseCafe.domain.menu.MenuStatus.HOT;
import static stable.horseCafe.domain.menu.MenuStatus.ICE;
import static stable.horseCafe.domain.menu.MenuType.COFFEE;

@SpringBootTest
@AutoConfigureMockMvc
class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MenuRepository menuRepository;

    @BeforeEach
    void clean() {
        menuRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "MEMBER")
    @DisplayName("메뉴 등록")
    void registerMenu() throws Exception {
        // given
        MenuSaveReqDto reqDto = MenuSaveReqDto.builder()
                .name("아메리카노")
                .price(4500)
                .stockQuantity(50)
                .menuType(COFFEE)
                .menuStatus(HOT)
                .build();

        String json = objectMapper.writeValueAsString(reqDto);

        // expected
        mockMvc.perform(post("/stable/v1/menu")
                .contentType(APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$.message").value("메뉴 등록"))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "MEMBER")
    @DisplayName("메뉴 수정")
    void editMenu() throws Exception {
        // given
        Menu menu = menuSave();
        MenuUpdateReqDto reqDto = MenuUpdateReqDto.builder()
                .name("아이스 아메리카노")
                .price(4800)
                .stockQuantity(70)
                .menuType(COFFEE)
                .menuStatus(ICE)
                .build();

        String json = objectMapper.writeValueAsString(reqDto);

        // expected
        mockMvc.perform(put("/stable/v1/menu/{menuId}", menu.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.message").value("메뉴 수정"))
                .andDo(print());
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
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "MEMBER")
    @DisplayName("메뉴 단건 조회")
    void getMenu() throws Exception {
        // given
        Menu menu = menuSave();

        // expected
        mockMvc.perform(get("/stable/v1/menu/{menuId}", menu.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("메뉴 단건 조회"))
                .andExpect(jsonPath("$.data.name").value("아메리카노"))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "MEMBER")
    @DisplayName("메뉴 목록 조회")
    void getMenuList() throws Exception {
        // given
        saveMenuList(); // menuType = ICE 5, HOT 5
        MenuSearchCondition cond = MenuSearchCondition.builder()
                .menuName(null)
                .menuType(null)
                .menuStatus(ICE)
                .build();

        // expected
        mockMvc.perform(get("/stable/v1/menuList")
                        .contentType(APPLICATION_JSON)
                        .param("menuStatus", cond.getMenuStatus().toString()))
                .andExpect(jsonPath("$.message").value("메뉴 목록 검색"))
                .andExpect(jsonPath("$.data.length()", is(5)))
                .andExpect(jsonPath("$.data[0].menuId").value(1L))
                .andExpect(jsonPath("$.data[1].menuId").value(3L))
                .andDo(print());
    }

    Menu menuSave() {
        return Menu.builder()
                .name("아이스 아메리카노")
                .price(4800)
                .stockQuantity(50)
                .menuType(COFFEE)
                .menuStatus(ICE)
                .build();
    }

    void saveMenuList() {
        List<Menu> menuList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Menu menu;
            if (i % 2 == 0) {
                menu = Menu.builder()
                        .name("아이스 아메리카노"+i)
                        .price(4800)
                        .stockQuantity(50)
                        .menuType(COFFEE)
                        .menuStatus(ICE)
                        .build();
            } else {
                menu = Menu.builder()
                        .name("아메리카노"+i)
                        .price(4500)
                        .stockQuantity(50)
                        .menuType(COFFEE)
                        .menuStatus(HOT)
                        .build();
            }
            menuList.add(menu);
        }
        menuRepository.saveAll(menuList);
    }
}
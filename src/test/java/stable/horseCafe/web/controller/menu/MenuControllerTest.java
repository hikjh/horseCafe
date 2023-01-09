package stable.horseCafe.web.controller.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
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
                .andExpect(jsonPath("$.data.name").value("아이스 아메리카노"))
                .andExpect(jsonPath("$.data.price").value(4800))
                .andExpect(jsonPath("$.data.menuType").value(COFFEE.toString()))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "MEMBER")
    @DisplayName("메뉴 목록 조회")
    @Order(1)
    void getMenuList() throws Exception {
        /**
         *  total Menu = 20개,
         *  menuType = ICE / HOT,
         *  - HOT menu = 10개 (홀수),
         *  - ICE menu = 10개 (짝수),
         *
         *  default page = 1
         *  default size = 5
         *
         *  test -> HOT menu, page = 2, size = 5, DESC id값
         */
        // given
        saveMenuList(); // menuType = ICE 10, HOT 10
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
                .andExpect(jsonPath("$.data.length()", is(5)))
                .andExpect(jsonPath("$.data[0].menuId").value(9))
                .andExpect(jsonPath("$.data[1].menuId").value(7L))
                .andExpect(jsonPath("$.data[4].menuId").value(1L))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "MEMBER")
    @DisplayName("메뉴 수정 - 예외처리 발생 테스트")
    void editMenu_exception() throws Exception {
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
        mockMvc.perform(put("/stable/v1/menu/{menuId}", menu.getId() + 1)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.message").value("존재하지 않는 메뉴입니다."))
                .andExpect(jsonPath("$.status").value(404))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "MEMBER")
    @DisplayName("메뉴 삭제 - 예외처리 발생 테스트")
    void deleteMenu_exception() throws Exception {
        // given
        Menu menu = menuSave();

        // expected
        mockMvc.perform(delete("/stable/v1/menu/{menuId}", menu.getId() + 1)
                        .contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("존재하지 않는 메뉴입니다."))
                .andExpect(jsonPath("$.status").value(404))
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "MEMBER")
    @DisplayName("메뉴 단건 조회 - 예외처리 발생 테스트")
    void getMenu_exception() throws Exception {
        // given
        Menu menu = menuSave();

        // expected
        mockMvc.perform(get("/stable/v1/menu/{menuId}", menu.getId() + 1)
                        .contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("존재하지 않는 메뉴입니다."))
                .andExpect(jsonPath("$.status").value(404))
                .andDo(print());
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

            String name;
            MenuStatus menuStatus;

            if (i % 2 != 0) {
                name = "아메리카노";
                menuStatus = HOT;
            } else {
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
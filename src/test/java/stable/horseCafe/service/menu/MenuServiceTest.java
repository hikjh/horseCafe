package stable.horseCafe.service.menu;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import stable.horseCafe.domain.menu.Menu;
import stable.horseCafe.domain.menu.MenuRepository;
import stable.horseCafe.web.common.exception.GlobalException;
import stable.horseCafe.web.dto.menu.MenuResDto;
import stable.horseCafe.web.dto.menu.MenuSaveReqDto;
import stable.horseCafe.web.dto.menu.MenuSearchCondition;
import stable.horseCafe.web.dto.menu.MenuUpdateReqDto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static stable.horseCafe.domain.menu.MenuStatus.HOT;
import static stable.horseCafe.domain.menu.MenuStatus.ICE;
import static stable.horseCafe.domain.menu.MenuType.COFFEE;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
class MenuServiceTest {

    @Autowired
    MenuService menuService;
    @Autowired
    MenuRepository menuRepository;

    @BeforeEach
    void clean() {
        menuRepository.deleteAll();
    }

    @Test
    @DisplayName("메뉴 등록")
    void registerMenu() {
        // given
        MenuSaveReqDto dto = MenuSaveReqDto.builder()
                .name("아메리카노")
                .price(4500)
                .stockQuantity(50)
                .menuType(COFFEE)
                .menuStatus(ICE)
                .build();

        // when
        Long savedId = menuService.registerMenu(dto);
        Menu menu = menuRepository.findById(savedId).get();

        // then
        assertNotNull(menu);
        assertEquals(dto.getName(), menu.getName());
        assertEquals(dto.getPrice(), menu.getPrice());
        assertEquals(dto.getMenuType(), menu.getMenuType());
        assertEquals(dto.getMenuStatus(), menu.getMenuStatus());
    }

    @Test
    @DisplayName("메뉴 수정 - null일 경우 기존 데이터 그대로 유지")
    void editMenu() {
        // given
        Menu menu = saveMenu();
        MenuUpdateReqDto reqDto = MenuUpdateReqDto
                .builder()
                .name("아메리카노")
                .price(4500)
                .menuType(COFFEE)
                .menuStatus(HOT)
                .build();

        // when
        menuService.editMenu(menu.getId(), reqDto);

        // then
        assertNotNull(menu);
        assertEquals("아메리카노", menu.getName());
        assertEquals(4500, menu.getPrice());
        assertEquals(50, menu.getStockQuantity());
        assertEquals(COFFEE, menu.getMenuType());
        assertEquals(HOT, menu.getMenuStatus());
    }

    @Test
    @DisplayName("메뉴 삭제")
    void deleteMenu() {
        // given
        Menu menu = saveMenu();

        // when
        menuService.deleteMenu(menu.getId());

        // then
        assertEquals(0, menuRepository.count());
    }

    @Test
    @DisplayName("메뉴 단건 조회")
    void getMenu() {
        // given
        Menu menu = saveMenu();

        // when
        MenuResDto resultMenu = menuService.getMenu(menu.getId());

        // then
        assertNotNull(resultMenu);
        assertEquals("아이스 아메리카노", resultMenu.getName());
        assertEquals(5000, resultMenu.getPrice());
        assertEquals(COFFEE, resultMenu.getMenuType());
        assertEquals(ICE, resultMenu.getMenuStatus());
    }

    @Test
    @DisplayName("메뉴 목록 검색 조회 - 페이징 테스트")
    @Order(1)
    void getMenuList() {
        // given
        saveMenuList();// 메뉴 20개 생성 - paging test
        MenuSearchCondition condition = MenuSearchCondition.builder()
                .menuName(null)
                .menuType(null)
                .menuStatus(HOT)
                .build();

        // when
        List<MenuResDto> resultMenuList = menuService.getMenuList(condition);

        // then
        assertEquals(5, resultMenuList.size());
        assertEquals(HOT, resultMenuList.get(0).getMenuStatus());
        assertEquals(20L, resultMenuList.get(0).getMenuId());
    }

    @Test
    @DisplayName("메뉴 수정 - 예외처리 발생 테스트")
    void editMenu_exception() {
        // given
        Menu menu = saveMenu();
        MenuUpdateReqDto reqDto = MenuUpdateReqDto
                .builder()
                .name("아메리카노")
                .price(4500)
                .menuType(COFFEE)
                .menuStatus(HOT)
                .build();

        // when
        GlobalException ge = assertThrows(GlobalException.class, () -> {
            menuService.editMenu(menu.getId() + 1, reqDto);
        });

        // then
        assertEquals("존재하지 않는 메뉴입니다.", ge.getMessage());
        assertEquals("NOT_FOUND", ge.getCode().toString());
    }

    @Test
    @DisplayName("메뉴 삭제 - 예외처리 발생 테스트")
    void deleteMenu_exception() {
        // given
        Menu menu = saveMenu();

        // when
        GlobalException ge = assertThrows(GlobalException.class, () -> {
            menuService.deleteMenu(menu.getId() + 1);
        });

        // then
        assertEquals("존재하지 않는 메뉴입니다.", ge.getMessage());
        assertEquals("NOT_FOUND", ge.getCode().toString());
    }

    @Test
    @DisplayName("메뉴 단건 조회 - 예외처리 발생 테스트")
    void getMenu_exception() {
        // given
        Menu menu = saveMenu();

        // when
        GlobalException ge = assertThrows(GlobalException.class, () -> {
            menuService.getMenu(menu.getId() + 1);
        });

        // then
        assertEquals("존재하지 않는 메뉴입니다.", ge.getMessage());
        assertEquals("NOT_FOUND", ge.getCode().toString());
    }

    private Menu saveMenu() {
        // given
        Menu menu = Menu.builder()
                .name("아이스 아메리카노")
                .price(5000)
                .stockQuantity(50)
                .menuType(COFFEE)
                .menuStatus(ICE)
                .build();
        menuRepository.save(menu);
        return menu;
    }

    void saveMenuList() {
        List<Menu> menuList = IntStream.range(0, 20)
                .mapToObj(i -> Menu.builder()
                        .name("아메리카노 "+i)
                        .price(4500)
                        .stockQuantity(50)
                        .menuType(COFFEE)
                        .menuStatus(HOT)
                        .build())
                .collect(Collectors.toList());

        menuRepository.saveAll(menuList);
    }
}
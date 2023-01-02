package stable.horseCafe.service.menu;

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
import stable.horseCafe.domain.menu.MenuStatus;
import stable.horseCafe.domain.menu.MenuType;
import stable.horseCafe.web.dto.menu.MenuResDto;
import stable.horseCafe.web.dto.menu.MenuSaveReqDto;
import stable.horseCafe.web.dto.menu.MenuSearchCondition;
import stable.horseCafe.web.dto.menu.MenuUpdateReqDto;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class MenuServiceTest {

    @Autowired
    MenuService menuService;
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    MemberRepository memberRepository;

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
                .menuType(MenuType.COFFEE)
                .menuStatus(MenuStatus.ICE)
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
    @DisplayName("메뉴 수정")
    void editMenu() {
        // given
        Menu menu = Menu.builder()
                .name("아이스 아메리카노")
                .price(5000)
                .stockQuantity(50)
                .menuType(MenuType.COFFEE)
                .menuStatus(MenuStatus.ICE)
                .build();
        menuRepository.save(menu);

        // when
        MenuUpdateReqDto reqDto = MenuUpdateReqDto
                .builder()
                .name("아메리카노")
                .price(4500)
                .stockQuantity(45)
                .menuType(MenuType.COFFEE)
                .menuStatus(MenuStatus.HOT)
                .build();
        menuService.editMenu(menu.getId(), reqDto);

        // then
        assertNotNull(menu);
        assertEquals("아메리카노", menu.getName());
        assertEquals(4500, menu.getPrice());
        assertEquals(45, menu.getStockQuantity());
        assertEquals(MenuType.COFFEE, menu.getMenuType());
        assertEquals(MenuStatus.HOT, menu.getMenuStatus());
    }

    @Test
    @DisplayName("메뉴 삭제")
    void deleteMenu() {
        // given
        Menu menu = Menu.builder()
                .name("아이스 아메리카노")
                .price(5000)
                .stockQuantity(50)
                .menuType(MenuType.COFFEE)
                .menuStatus(MenuStatus.ICE)
                .build();

        menuRepository.save(menu);

        // when
        menuService.deleteMenu(menu.getId());

        // then
        //assertNull(menu.getName());
    }

    @Test
    @DisplayName("메뉴 단건 조회")
    void getMenu() {
        // given
        Menu menu = Menu.builder()
                .name("아이스 아메리카노")
                .price(5000)
                .stockQuantity(50)
                .menuType(MenuType.COFFEE)
                .menuStatus(MenuStatus.ICE)
                .build();
        menuRepository.save(menu);

        // when
        MenuResDto resultMenu = menuService.getMenu(menu.getId());

        // then
        assertNotNull(resultMenu);
        assertEquals("아이스 아메리카노", resultMenu.getName());
        assertEquals(5000, resultMenu.getPrice());
        assertEquals(MenuType.COFFEE, resultMenu.getMenuType());
        assertEquals(MenuStatus.ICE, resultMenu.getMenuStatus());
    }


    @Test
    @DisplayName("메뉴 목록 검색 조회 - 전체, 메뉴 유형, 메뉴 상태")
    void getMenuList() {
        // given
        List<Menu> menuList = saveMenuList();// 메뉴 10개 생성 - 5개: ICE, 5개: HOT
        menuRepository.saveAll(menuList);

        MenuSearchCondition condition = MenuSearchCondition.builder()
                .menuName(null)
                .menuType(null)
                .menuStatus(MenuStatus.HOT)
                .build();
        
        // when
        List<MenuResDto> resultMenuList = menuService.getMenuList(condition);

        // then
        assertEquals(5, resultMenuList.size());
        assertEquals(MenuStatus.HOT, resultMenuList.get(0).getMenuStatus());
    }

    private List<Menu> saveMenuList() {
        List<Menu> menuList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Menu menu;
            if (i % 2 == 0) {
                menu = Menu.builder()
                        .name("아이스 아메리카노" + i)
                        .price(5000)
                        .stockQuantity(50)
                        .menuType(MenuType.COFFEE)
                        .menuStatus(MenuStatus.HOT)
                        .build();
            } else {
                menu = Menu.builder()
                        .name("아이스 아메리카노" + i)
                        .price(5000)
                        .stockQuantity(50)
                        .menuType(MenuType.COFFEE)
                        .menuStatus(MenuStatus.ICE)
                        .build();
            }
            menuList.add(menu);
        }

        return menuList;
    }
}
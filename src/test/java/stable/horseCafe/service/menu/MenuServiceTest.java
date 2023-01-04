package stable.horseCafe.service.menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import stable.horseCafe.domain.member.MemberRepository;
import stable.horseCafe.domain.menu.Menu;
import stable.horseCafe.domain.menu.MenuRepository;
import stable.horseCafe.web.dto.menu.MenuResDto;
import stable.horseCafe.web.dto.menu.MenuSaveReqDto;
import stable.horseCafe.web.dto.menu.MenuSearchCondition;
import stable.horseCafe.web.dto.menu.MenuUpdateReqDto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static stable.horseCafe.domain.menu.MenuStatus.HOT;
import static stable.horseCafe.domain.menu.MenuStatus.ICE;
import static stable.horseCafe.domain.menu.MenuType.COFFEE;


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
    @DisplayName("메뉴 수정")
    void editMenu() {
        // given
        Menu menu = Menu.builder()
                .name("아이스 아메리카노")
                .price(5000)
                .stockQuantity(50)
                .menuType(COFFEE)
                .menuStatus(ICE)
                .build();
        menuRepository.save(menu);

        // when
        MenuUpdateReqDto reqDto = MenuUpdateReqDto
                .builder()
                .name("아메리카노")
                .price(4500)
                .stockQuantity(45)
                .menuType(COFFEE)
                .menuStatus(HOT)
                .build();
        menuService.editMenu(menu.getId(), reqDto);

        // then
        assertNotNull(menu);
        assertEquals("아메리카노", menu.getName());
        assertEquals(4500, menu.getPrice());
        assertEquals(45, menu.getStockQuantity());
        assertEquals(COFFEE, menu.getMenuType());
        assertEquals(HOT, menu.getMenuStatus());
    }

    @Test
    @DisplayName("메뉴 삭제")
    void deleteMenu() {
        // given
        Menu menu = Menu.builder()
                .name("아이스 아메리카노")
                .price(5000)
                .stockQuantity(50)
                .menuType(COFFEE)
                .menuStatus(ICE)
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
                .menuType(COFFEE)
                .menuStatus(ICE)
                .build();
        menuRepository.save(menu);

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
    @DisplayName("메뉴 목록 검색 조회 - 전체, 메뉴 유형, 메뉴 상태")
    void getMenuList() {
        // given
        saveMenuList();// 메뉴 10개 생성 - 5개: ICE, 5개: HOT
        MenuSearchCondition condition = MenuSearchCondition.builder()
                .menuName(null)
                .menuType(null)
                .menuStatus(HOT)
                .build();

        // when
        List<MenuResDto> resultMenuList = menuService.getMenuList(condition);

        // then
        assertEquals(10, resultMenuList.size());
        assertEquals(HOT, resultMenuList.get(0).getMenuStatus());
    }

    void saveMenuList() {
        List<Menu> menuList = IntStream.range(0, 10)
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
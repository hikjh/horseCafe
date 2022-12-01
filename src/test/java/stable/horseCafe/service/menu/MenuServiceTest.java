package stable.horseCafe.service.menu;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import stable.horseCafe.domain.menu.Menu;
import stable.horseCafe.domain.menu.MenuRepository;
import stable.horseCafe.domain.menu.MenuStatus;
import stable.horseCafe.domain.menu.MenuType;
import stable.horseCafe.service.member.MemberService;
import stable.horseCafe.web.dto.menu.MenuSaveReqDto;
import stable.horseCafe.web.dto.menu.MenuUpdateReqDto;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class MenuServiceTest {

    @Autowired
    MenuService menuService;
    @Autowired
    MenuRepository menuRepository;

    @Test
    void 메뉴_등록_수정() {

        MenuSaveReqDto dto = MenuSaveReqDto.builder()
                .name("아메리카노")
                .price(4500)
                .stockQuantity(50)
                .menuType(MenuType.COFFEE)
                .menuStatus(MenuStatus.ICE)
                .build();

        Long savedId = menuService.registerMenu(dto);

        MenuUpdateReqDto updateReqDto = MenuUpdateReqDto.builder()
                .name("아메리카노22")
                .price(5500)
                .stockQuantity(100)
                .menuType(MenuType.COFFEE)
                .menuStatus(MenuStatus.HOT)
                .build();

        Long updatedId = menuService.editMenu(savedId, updateReqDto);
        Menu menu = menuRepository.findById(updatedId).get();

        assertNotNull(savedId);
        assertEquals(menu.getName(), updateReqDto.getName());
        assertEquals(menu.getMenuType(), updateReqDto.getMenuType());
    }

    @Test
    void 메뉴_삭제() {
        MenuSaveReqDto dto = MenuSaveReqDto.builder()
                .name("아메리카노")
                .price(4500)
                .stockQuantity(50)
                .menuType(MenuType.COFFEE)
                .menuStatus(MenuStatus.ICE)
                .build();

        Long savedId = menuService.registerMenu(dto);
        Long result = menuService.deleteMenu(savedId);

        assertEquals(result, savedId);
    }
}
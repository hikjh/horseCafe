package stable.horseCafe.service.menu;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stable.horseCafe.domain.menu.Menu;
import stable.horseCafe.domain.menu.MenuRepository;
import stable.horseCafe.web.common.exception.MenuNotFoundException;
import stable.horseCafe.web.dto.menu.MenuResDto;
import stable.horseCafe.web.dto.menu.MenuSaveReqDto;
import stable.horseCafe.web.dto.menu.MenuSearchCondition;
import stable.horseCafe.web.dto.menu.MenuUpdateReqDto;

import java.util.List;


@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    /**
     *  메뉴 등록
     */
    @Transactional
    public Long registerMenu(MenuSaveReqDto dto) {
        return menuRepository.save(dto.toEntity()).getId();
    }

    /**
     *  메뉴 수정
     */
    @Transactional
    public Long editMenu(Long menuId, MenuUpdateReqDto dto) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(MenuNotFoundException::new);

        menu.update(
                dto.getName() != null ? dto.getName() : menu.getName(),
                dto.getPrice() != null ? dto.getPrice() : menu.getPrice(),
                dto.getStockQuantity() != null ? dto.getStockQuantity() : menu.getStockQuantity(),
                dto.getMenuType() != null ? dto.getMenuType() : menu.getMenuType(),
                dto.getMenuStatus() != null ? dto.getMenuStatus() : menu.getMenuStatus());

        return menuId;
    }

    /**
     *  메뉴 삭제
     */
    @Transactional
    public Long deleteMenu(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(MenuNotFoundException::new);

        menuRepository.delete(menu);
        return menuId;
    }

    /**
     *  메뉴 단건 조회
     */
    public MenuResDto getMenu(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(MenuNotFoundException::new);
        return MenuResDto.builder()
                .menu(menu)
                .build();
    }

    /**
     *  유형별 메뉴 목록 조회
     */
    public List<MenuResDto> getMenuList(MenuSearchCondition cond) {
        return menuRepository.getMenuList(cond);
    }
}

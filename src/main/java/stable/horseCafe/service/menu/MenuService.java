package stable.horseCafe.service.menu;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stable.horseCafe.domain.menu.Menu;
import stable.horseCafe.domain.menu.MenuRepository;
import stable.horseCafe.web.common.exception.GlobalException;
import stable.horseCafe.web.common.response.code.ResponseCode;
import stable.horseCafe.web.dto.menu.MenuResDto;
import stable.horseCafe.web.dto.menu.MenuSaveReqDto;
import stable.horseCafe.web.dto.menu.MenuUpdateReqDto;



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
                .orElseThrow(() -> {
                    throw new GlobalException(ResponseCode.BAD_REQUEST, "존재하지 않는 메뉴입니다.");
                });

        menu.update(dto.getName(), dto.getPrice(), dto.getStockQuantity(), dto.getMenuType(), dto.getMenuStatus());
        return menuId;
    }

    /**
     *  메뉴 삭제
     */
    @Transactional
    public Long deleteMenu(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> {
                    throw new GlobalException(ResponseCode.BAD_REQUEST, "존재하지 않는 메뉴입니다.");
                });

        menuRepository.delete(menu);
        return menuId;
    }

    /**
     *  메뉴 단건 조회
     */
    public MenuResDto getMenu(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> {
                    throw new GlobalException(ResponseCode.BAD_REQUEST, "존재하지 않는 메뉴입니다.");
                });
        return new MenuResDto(menu);
    }
}

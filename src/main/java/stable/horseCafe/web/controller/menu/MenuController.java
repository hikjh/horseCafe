package stable.horseCafe.web.controller.menu;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import stable.horseCafe.service.menu.MenuService;
import stable.horseCafe.web.common.response.CommonResponse;
import stable.horseCafe.web.common.util.ResponseUtil;
import stable.horseCafe.web.dto.menu.MenuSaveReqDto;
import stable.horseCafe.web.dto.menu.MenuSearchCondition;
import stable.horseCafe.web.dto.menu.MenuUpdateReqDto;

@RestController
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    /**
     *  메뉴 등록
     */
    @PostMapping("/stable/v1/menu")
    public CommonResponse registerMenu(@RequestBody MenuSaveReqDto dto) {
        return ResponseUtil.getSingleResult("메뉴 등록", menuService.registerMenu(dto));
    }

    /**
     *  메뉴 수정
     */
    @PutMapping("/stable/v1/menu/{menuId}")
    public CommonResponse editMenu(@PathVariable Long menuId, @RequestBody MenuUpdateReqDto dto) {
        return ResponseUtil.getSingleResult("메뉴 수정", menuService.editMenu(menuId, dto));
    }

    /**
     *  메뉴 삭제
     */
    @DeleteMapping("/stable/v1/menu/{menuId}")
    public CommonResponse editMenu(@PathVariable Long menuId) {
        return ResponseUtil.getSingleResult("메뉴 삭제", menuService.deleteMenu(menuId));
    }

    /**
     *  메뉴 단건 조회
     */
    @GetMapping("/stable/v1/menu/{menuId}")
    public CommonResponse getMenu(@PathVariable Long menuId) {
        return ResponseUtil.getSingleResult("메뉴 단건 조회", menuService.getMenu(menuId));
    }

    /**
     *  메뉴 목록 검색 조회
     *  - 조건 : 전체, 메뉴 유형(COFFEE, TEA, ADE), 메뉴 상태(ICE, HOT)
     */
    @GetMapping("/stable/v1/menuList")
    public CommonResponse getMenuList(MenuSearchCondition cond) {
        return ResponseUtil.getSingleResult("유형별 메뉴 목록 조회", menuService.getMenuList(cond));
    }
}

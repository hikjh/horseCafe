package stable.horseCafe.domain.menu.custom;


import stable.horseCafe.web.dto.menu.MenuResDto;
import stable.horseCafe.web.dto.menu.MenuSearchCondition;

import java.util.List;

public interface CustomMenuRepository {
    List<MenuResDto> getMenuList(MenuSearchCondition cond);
}

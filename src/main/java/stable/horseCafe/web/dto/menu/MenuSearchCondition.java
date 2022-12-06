package stable.horseCafe.web.dto.menu;

import lombok.Getter;
import stable.horseCafe.domain.menu.MenuStatus;
import stable.horseCafe.domain.menu.MenuType;

@Getter
public class MenuSearchCondition {

    private MenuType menuType;
    private MenuStatus menuStatus;

    public MenuSearchCondition(MenuType menuType, MenuStatus menuStatus) {
        this.menuType = menuType;
        this.menuStatus = menuStatus;
    }
}

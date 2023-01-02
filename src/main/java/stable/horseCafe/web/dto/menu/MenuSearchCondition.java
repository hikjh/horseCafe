package stable.horseCafe.web.dto.menu;

import lombok.Builder;
import lombok.Getter;
import stable.horseCafe.domain.menu.MenuStatus;
import stable.horseCafe.domain.menu.MenuType;

@Getter
public class MenuSearchCondition {

    private String menuName;
    private MenuType menuType;
    private MenuStatus menuStatus;

    @Builder
    public MenuSearchCondition(String menuName, MenuType menuType, MenuStatus menuStatus) {
        this.menuName = menuName;
        this.menuType = menuType;
        this.menuStatus = menuStatus;
    }
}

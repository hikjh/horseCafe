package stable.horseCafe.web.dto.menu;

import lombok.Builder;
import lombok.Getter;
import stable.horseCafe.domain.menu.MenuStatus;
import stable.horseCafe.domain.menu.MenuType;

import static java.lang.Math.*;

@Getter
public class MenuSearchCondition {

    private static final int MAX_SIZE = 2000;
    private Integer page;
    private Integer size;
    private String menuName;
    private MenuType menuType;
    private MenuStatus menuStatus;

    @Builder
    public MenuSearchCondition(String menuName, MenuType menuType, MenuStatus menuStatus, Integer page, Integer size) {
        this.menuName = menuName;
        this.menuType = menuType;
        this.menuStatus = menuStatus;

        this.page = (page == null ? 1 : page);
        this.size = (size == null ? 5 : size);
    }

    public Long getOffset() {
        return (long) min(1, this.page- 1) * min(size, MAX_SIZE);
    }
}

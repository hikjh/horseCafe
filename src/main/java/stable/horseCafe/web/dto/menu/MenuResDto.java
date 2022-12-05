package stable.horseCafe.web.dto.menu;

import lombok.Getter;
import stable.horseCafe.domain.menu.Menu;
import stable.horseCafe.domain.menu.MenuStatus;
import stable.horseCafe.domain.menu.MenuType;

@Getter
public class MenuResDto {

    private Long menuId;
    private String name;
    private int price;
    private int stockQuantity;
    private MenuType menuType;
    private MenuStatus menuStatus;

    public MenuResDto(Menu menu) {
        this.menuId = menu.getId();
        this.name = menu.getName();
        this.price = menu.getPrice();
        this.stockQuantity = menu.getStockQuantity();
        this.menuType = menu.getMenuType();
        this.menuStatus = menu.getMenuStatus();
    }
}

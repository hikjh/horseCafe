package stable.horseCafe.web.dto.menu;

import lombok.Getter;
import stable.horseCafe.domain.menu.MenuType;

@Getter
public class MenuUpdateReqDto {

    private String name;
    private int price;
    private int stockQuantity;
    private MenuType menuType;

    public MenuUpdateReqDto(String name, int price, int stockQuantity, MenuType menuType) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.menuType = menuType;
    }
}

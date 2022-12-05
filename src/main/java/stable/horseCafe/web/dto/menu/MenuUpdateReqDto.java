package stable.horseCafe.web.dto.menu;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import stable.horseCafe.domain.menu.MenuStatus;
import stable.horseCafe.domain.menu.MenuType;

@Getter
@NoArgsConstructor
public class MenuUpdateReqDto {

    private String name;
    private int price;
    private int stockQuantity;
    private MenuType menuType;
    private MenuStatus menuStatus;

    @Builder
    public MenuUpdateReqDto(String name, int price, int stockQuantity, MenuType menuType, MenuStatus menuStatus) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.menuType = menuType;
        this.menuStatus = menuStatus;
    }
}

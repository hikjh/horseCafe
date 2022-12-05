package stable.horseCafe.web.dto.menu;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import stable.horseCafe.domain.menu.Menu;
import stable.horseCafe.domain.menu.MenuStatus;
import stable.horseCafe.domain.menu.MenuType;

@Getter
@NoArgsConstructor
public class MenuSaveReqDto {

    private String name;
    private int price;
    private int stockQuantity;
    private MenuType menuType;
    private MenuStatus menuStatus;

    @Builder
    public MenuSaveReqDto(String name, int price, int stockQuantity, MenuType menuType, MenuStatus menuStatus) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.menuType = menuType;
        this.menuStatus = menuStatus;
    }

    public Menu toEntity() {
        return Menu.builder()
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .menuType(menuType)
                .menuStatus(menuStatus)
                .build();
    }
}

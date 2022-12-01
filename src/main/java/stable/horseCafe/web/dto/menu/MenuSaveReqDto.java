package stable.horseCafe.web.dto.menu;

import lombok.Builder;
import lombok.Getter;
import stable.horseCafe.domain.menu.Menu;
import stable.horseCafe.domain.menu.MenuType;

@Getter
public class MenuSaveReqDto {

    private String name;
    private int price;
    private int stockQuantity;
    private MenuType menuType;

    @Builder
    public MenuSaveReqDto(String name, int price, int stockQuantity, MenuType menuType) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.menuType = menuType;
    }

    public Menu toEntity() {
        return Menu.builder()
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .menuType(menuType)
                .build();
    }
}

package stable.horseCafe.web.dto.menu;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import stable.horseCafe.domain.menu.Menu;
import stable.horseCafe.domain.menu.MenuStatus;
import stable.horseCafe.domain.menu.MenuType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class MenuSaveReqDto {

    @NotBlank(message = "이름은 필수값입니다.")
    private String name;
    @NotNull(message = "가격은 필수값입니다.")
    private int price;
    @NotNull(message = "재고수량은 필수값입니다.")
    private int stockQuantity;
    @NotNull(message = "메뉴유형은 필수값입니다.")
    private MenuType menuType;
    @NotNull(message = "메뉴상태는 필수값입니다.")
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

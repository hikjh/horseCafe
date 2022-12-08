package stable.horseCafe.web.dto.orderMenu;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import stable.horseCafe.domain.menu.MenuStatus;

@Getter
@NoArgsConstructor
public class OrderMenuResDto {

    private String name;
    private int count;
    private int price;
    private MenuStatus status;

    @QueryProjection
    public OrderMenuResDto(String name, int count, int price, MenuStatus status) {
        this.name = name;
        this.count = count;
        this.price = price;
        this.status = status;
    }
}

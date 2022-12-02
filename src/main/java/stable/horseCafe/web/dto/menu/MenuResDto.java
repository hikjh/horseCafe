package stable.horseCafe.web.dto.menu;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import stable.horseCafe.domain.menu.Menu;
import stable.horseCafe.domain.menu.MenuStatus;
import stable.horseCafe.domain.menu.MenuType;
import stable.horseCafe.domain.review.Review;
import stable.horseCafe.web.dto.review.ReviewResDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MenuResDto {

    private Long menuId;
    private String name;
    private int price;
    private int stockQuantity;
    private MenuType menuType;
    private MenuStatus menuStatus;
    private List<ReviewResDto> reviews;

    public MenuResDto(Menu menu) {
        this.menuId = menu.getId();
        this.name = menu.getName();
        this.price = menu.getPrice();
        this.stockQuantity = menu.getStockQuantity();
        this.menuType = menu.getMenuType();
        this.menuStatus = menu.getMenuStatus();
        this.reviews = menu.getReviews().stream()
                .map(review -> new ReviewResDto(review))
                .collect(Collectors.toList());
    }
}

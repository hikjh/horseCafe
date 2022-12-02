package stable.horseCafe.domain.menu;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import stable.horseCafe.domain.BaseTimeEntity;
import stable.horseCafe.domain.review.Review;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

@Getter
@NoArgsConstructor
@ToString
@Entity
public class Menu extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "menu_id")
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;
    @Enumerated(EnumType.STRING)
    private MenuType menuType;
    @Enumerated(EnumType.STRING)
    private MenuStatus menuStatus;
    @OneToMany(fetch = LAZY, mappedBy = "menu", cascade = ALL)
    private List<Review> reviews = new ArrayList<>();

    @Builder
    public Menu(String name, int price, int stockQuantity, MenuType menuType, MenuStatus menuStatus) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.menuType = menuType;
        this.menuStatus = menuStatus;
    }

    public void addReview(Review review) {
        this.reviews.add(review);
    }

    public void update(String name, int price, int stockQuantity, MenuType menuType, MenuStatus menuStatus) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.menuType = menuType;
        this.menuStatus = menuStatus;
    }
}

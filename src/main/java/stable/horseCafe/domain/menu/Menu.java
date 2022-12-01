package stable.horseCafe.domain.menu;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import stable.horseCafe.domain.BaseTimeEntity;
import stable.horseCafe.domain.review.Review;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

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
    //private List<Review> reviews = new ArrayList<>();

    @Builder
    public Menu(String name, int price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }
/*
    public void addReview(Review review) {
        if (review != null) {
            this.reviews.add(review);
        }
    }
 */
}

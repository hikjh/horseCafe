package stable.horseCafe.domain.menu;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import stable.horseCafe.domain.BaseTimeEntity;

import javax.persistence.*;

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

    @Builder
    public Menu(String name, int price, int stockQuantity, MenuType menuType, MenuStatus menuStatus) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.menuType = menuType;
        this.menuStatus = menuStatus;
    }

    public void update(String name, int price, int stockQuantity, MenuType menuType) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.menuType = menuType;
    }
}

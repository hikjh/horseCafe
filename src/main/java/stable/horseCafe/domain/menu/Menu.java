package stable.horseCafe.domain.menu;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import stable.horseCafe.domain.BaseTimeEntity;
import stable.horseCafe.web.common.exception.GlobalException;
import stable.horseCafe.web.common.response.code.ResponseCode;

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

    public void update(String name, int price, int stockQuantity, MenuType menuType, MenuStatus menuStatus) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.menuType = menuType;
        this.menuStatus = menuStatus;
    }

    /**
     *  주문 시 재고수량 감소
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity -= quantity;
        if (restStock < 0) {
            throw new GlobalException(ResponseCode.BAD_REQUEST, "남은 재고가 없습니다.");
        }
        this.stockQuantity = restStock;
    }

    /**
     *  주문 취소 시 재고수량 증가
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }
}

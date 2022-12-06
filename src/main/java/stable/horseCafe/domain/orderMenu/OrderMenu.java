package stable.horseCafe.domain.orderMenu;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import stable.horseCafe.domain.BaseTimeEntity;
import stable.horseCafe.domain.menu.Menu;
import stable.horseCafe.domain.order.Order;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Getter
@NoArgsConstructor
@ToString
@Entity
public class OrderMenu extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "order_menu_id")
    private Long id;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;
    private int count;
    private int orderMenuPrice;

    @Builder
    public OrderMenu(Menu menu, int count, int orderMenuPrice) {
        this.menu = menu;
        this.count = count;
        this.orderMenuPrice = orderMenuPrice;
    }

    /**
     *  주문
     */
    public void addOrder(Order order) {
        this.order = order;
        menu.removeStock(count);
    }

    /**
     *  주문 취소
     */
    public void cancel() {
        menu.addStock(count);
    }

    /**
     *  주문건 전체 가격
     */
    public int getTotalPrice() {
        return orderMenuPrice * count;
    }
}

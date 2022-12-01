package stable.horseCafe.domain.order;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import stable.horseCafe.domain.BaseTimeEntity;
import stable.horseCafe.domain.member.Member;
import stable.horseCafe.domain.orderMenu.OrderMenu;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

@Getter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "orders")
public class Order extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = ALL)
    private List<OrderMenu> orderMenus = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Builder
    public Order(Member member, List<OrderMenu> orderMenus) {
        this.member = member;
        for (OrderMenu orderMenu : orderMenus) {
            this.orderMenus.add(orderMenu);
            orderMenu.addOrder(this);
        }
        this.orderStatus = OrderStatus.ORDER;
    }
}

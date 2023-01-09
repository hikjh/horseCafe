package stable.horseCafe.web.dto.order;

import lombok.Builder;
import lombok.Getter;
import stable.horseCafe.domain.order.OrderStatus;

@Getter
public class OrderSearchCondition {

    private OrderStatus orderStatus;

    @Builder
    public OrderSearchCondition(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}

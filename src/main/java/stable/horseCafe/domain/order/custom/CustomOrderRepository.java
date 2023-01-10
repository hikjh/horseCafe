package stable.horseCafe.domain.order.custom;

import stable.horseCafe.domain.order.Order;
import stable.horseCafe.domain.order.OrderStatus;
import stable.horseCafe.web.dto.order.OrderResDto;
import stable.horseCafe.web.dto.order.OrderSearchCondition;

import java.util.List;
import java.util.Optional;

public interface CustomOrderRepository {
    Optional<Order> findFetchById(String email, Long orderId);

    List<OrderResDto> findOrderList(String email, OrderSearchCondition cond);

    OrderStatus findOrderStatus(String email, Long menuId);
}

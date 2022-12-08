package stable.horseCafe.domain.order.custom;

import stable.horseCafe.domain.order.Order;

import java.util.Optional;

public interface CustomOrderRepository {
    Optional<Order> findFetchById(Long orderId);
}

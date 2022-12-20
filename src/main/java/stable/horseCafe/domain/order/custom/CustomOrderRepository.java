package stable.horseCafe.domain.order.custom;

import stable.horseCafe.domain.order.Order;
import stable.horseCafe.web.dto.order.OrderResDto;

import java.util.List;
import java.util.Optional;

public interface CustomOrderRepository {
    Optional<Order> findFetchById(Long orderId);

    List<OrderResDto> findOrderList(String email);
}

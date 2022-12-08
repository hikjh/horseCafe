package stable.horseCafe.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;
import stable.horseCafe.domain.order.custom.CustomOrderRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, CustomOrderRepository {
}

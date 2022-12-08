package stable.horseCafe.domain.order.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import stable.horseCafe.domain.menu.QMenu;
import stable.horseCafe.domain.order.Order;
import stable.horseCafe.domain.order.QOrder;
import stable.horseCafe.domain.orderMenu.QOrderMenu;

import java.util.Optional;

import static stable.horseCafe.domain.menu.QMenu.*;
import static stable.horseCafe.domain.order.QOrder.*;
import static stable.horseCafe.domain.orderMenu.QOrderMenu.*;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements CustomOrderRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Order> findFetchById(Long orderId) {

        Order resultOrder = queryFactory
                .selectFrom(order)
                .join(order.orderMenus, orderMenu).fetchJoin()
                .join(orderMenu.menu, menu).fetchJoin()
                .where(order.id.eq(orderId))
                .fetchOne();

        return Optional.of(resultOrder);
    }
}

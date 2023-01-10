package stable.horseCafe.domain.order.custom;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import stable.horseCafe.domain.order.Order;
import stable.horseCafe.domain.order.OrderStatus;
import stable.horseCafe.web.dto.order.OrderResDto;
import stable.horseCafe.web.dto.order.OrderSearchCondition;
import stable.horseCafe.web.dto.order.QOrderResDto;
import stable.horseCafe.web.dto.orderMenu.QOrderMenuResDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static stable.horseCafe.domain.member.QMember.member;
import static stable.horseCafe.domain.menu.QMenu.menu;
import static stable.horseCafe.domain.order.QOrder.order;
import static stable.horseCafe.domain.orderMenu.QOrderMenu.orderMenu;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements CustomOrderRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Order> findFetchById(String email, Long orderId) {

        Order resultOrder = queryFactory
                .selectFrom(order)
                .join(order.member, member).fetchJoin()
                .join(order.orderMenus, orderMenu).fetchJoin()
                .join(orderMenu.menu, menu).fetchJoin()
                .where(
                        member.email.eq(email),
                        order.id.eq(orderId)
                )
                .fetchOne();

        return Optional.of(resultOrder);
    }

    @Override
    public List<OrderResDto> findOrderList(String email, OrderSearchCondition cond) {

        Map<Long, OrderResDto> transform = queryFactory
                .from(order)
                .innerJoin(order.member, member)
                .innerJoin(order.orderMenus, orderMenu)
                .innerJoin(orderMenu.menu, menu)
                .where(
                        member.email.eq(email),
                        orderStatusEq(cond.getOrderStatus())
                )
                .transform(groupBy(order.id).as(new QOrderResDto(
                        order.id,
                        member.name,
                        order.orderStatus,
                        list(new QOrderMenuResDto(
                                menu.name,
                                orderMenu.count,
                                menu.price,
                                menu.menuStatus
                        )),
                        order.createDate,
                        order.totalPrice
                )));

        return transform.keySet().stream()
                .map(transform::get)
                .collect(Collectors.toList());
    }

    private BooleanExpression orderStatusEq(OrderStatus orderStatus) {
        return orderStatus != null ? order.orderStatus.eq(orderStatus) : null;
    }

    @Override
    public OrderStatus findOrderStatus(String email, Long menuId) {
        return queryFactory
                .select(order.orderStatus)
                .from(order)
                .join(order.orderMenus, orderMenu)
                .where(
                        order.member.email.eq(email),
                        orderMenu.menu.id.eq(menuId))
                .fetchOne();
    }
}

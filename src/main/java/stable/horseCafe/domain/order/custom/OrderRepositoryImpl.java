package stable.horseCafe.domain.order.custom;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import stable.horseCafe.domain.order.Order;
import stable.horseCafe.domain.order.OrderStatus;
import stable.horseCafe.web.dto.order.OrderResDto;
import stable.horseCafe.web.dto.order.OrderSearchCondition;
import stable.horseCafe.web.dto.order.QOrderResDto;
import stable.horseCafe.web.dto.orderMenu.OrderMenuResDto;
import stable.horseCafe.web.dto.orderMenu.QOrderMenuResDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static stable.horseCafe.domain.member.QMember.*;
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
    public OrderStatus findOrderStatus(Long memberId, Long menuId) {
        return queryFactory
                .select(order.orderStatus)
                .from(order)
                .join(order.orderMenus, orderMenu)
                .where(
                        order.member.id.eq(memberId),
                        orderMenu.menu.id.eq(menuId))
                .fetchOne();
    }
}

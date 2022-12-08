package stable.horseCafe.domain.order.custom;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import stable.horseCafe.domain.member.Member;
import stable.horseCafe.domain.member.QMember;
import stable.horseCafe.domain.menu.QMenu;
import stable.horseCafe.domain.order.Order;
import stable.horseCafe.domain.order.QOrder;
import stable.horseCafe.domain.orderMenu.QOrderMenu;
import stable.horseCafe.web.dto.order.OrderResDto;
import stable.horseCafe.web.dto.order.QOrderResDto;
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
    public List<OrderResDto> findOrderList(String email) {

        Map<Long, OrderResDto> transform = queryFactory
                .selectFrom(order)
                .innerJoin(order.member, member)
                .innerJoin(order.orderMenus, orderMenu)
                .innerJoin(orderMenu.menu, menu)
                .where(
                        member.email.eq(email)
                )
                .transform(groupBy(order.id).as(new QOrderResDto(
                        order.id,
                        member.name.as("memberName"),
                        order.orderStatus,
                        list(new QOrderMenuResDto(
                                menu.name,
                                orderMenu.count,
                                menu.price,
                                menu.menuStatus
                        )),
                        order.createDate
                )));

        return transform.keySet()
                .stream().map(transform::get)
                .collect(Collectors.toList());
    }
}

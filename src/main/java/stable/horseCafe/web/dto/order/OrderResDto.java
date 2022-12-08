package stable.horseCafe.web.dto.order;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import stable.horseCafe.domain.order.OrderStatus;
import stable.horseCafe.web.dto.orderMenu.OrderMenuResDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class OrderResDto {

    private Long orderId;
    private String memberName;
    private OrderStatus orderStatus;
    private List<OrderMenuResDto> orderMenus;
    private LocalDateTime orderTime;

    @QueryProjection
    public OrderResDto(Long orderId, String memberName, OrderStatus orderStatus, List<OrderMenuResDto> orderMenus, LocalDateTime orderTime) {
        this.orderId = orderId;
        this.memberName = memberName;
        this.orderStatus = orderStatus;
        this.orderMenus = orderMenus;
        this.orderTime = orderTime;
    }
}

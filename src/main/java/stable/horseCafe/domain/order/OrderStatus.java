package stable.horseCafe.domain.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    ORDER("ORDER", "주문상태"),
    CANCEL("CANCEL","취소상태");

    private final String key;
    private final String value;
}

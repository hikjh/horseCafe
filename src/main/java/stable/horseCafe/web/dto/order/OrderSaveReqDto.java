package stable.horseCafe.web.dto.order;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderSaveReqDto {

    private Long menuId;
    private int count;

    @Builder
    public OrderSaveReqDto(Long menuId, int count) {
        this.menuId = menuId;
        this.count = count;
    }
}

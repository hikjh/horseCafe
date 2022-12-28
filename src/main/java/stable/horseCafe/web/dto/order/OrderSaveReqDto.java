package stable.horseCafe.web.dto.order;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class OrderSaveReqDto {

    @NotNull(message = "주문 번호는 필수값입니다.")
    private Long menuId;
    @NotNull(message = "주문 수량은 필수값입니다.")
    private int count;

    @Builder
    public OrderSaveReqDto(Long menuId, int count) {
        this.menuId = menuId;
        this.count = count;
    }
}

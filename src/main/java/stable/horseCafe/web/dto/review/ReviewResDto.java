package stable.horseCafe.web.dto.review;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ReviewResDto {

    private Long menuId;
    private Long reviewId;
    private String memberName;
    private String content;

    @QueryProjection
    public ReviewResDto(Long menuId, Long reviewId, String memberName, String content) {
        this.menuId = menuId;
        this.reviewId = reviewId;
        this.memberName = memberName;
        this.content = content;
    }
}

package stable.horseCafe.web.dto.review;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;
import stable.horseCafe.domain.review.Review;

@Getter
@ToString
public class ReviewResDto {

    private Long reviewId;
    private String memberName;
    private String content;

    public ReviewResDto(Review review) {
        this.reviewId = review.getId();
        this.memberName = review.getMember().getName();
        this.content = review.getContent();
    }

    @QueryProjection
    public ReviewResDto(Long reviewId, String memberName, String content) {
        this.reviewId = reviewId;
        this.memberName = memberName;
        this.content = content;
    }
}

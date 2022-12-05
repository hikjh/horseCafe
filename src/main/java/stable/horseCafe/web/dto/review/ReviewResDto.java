package stable.horseCafe.web.dto.review;

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
}

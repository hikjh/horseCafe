package stable.horseCafe.domain.review.custom;


import stable.horseCafe.web.dto.review.ReviewResDto;

import java.util.List;

public interface CustomReviewRepository {
    List<ReviewResDto> findMyReviewList(String email);
}

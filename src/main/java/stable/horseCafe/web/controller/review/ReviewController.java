package stable.horseCafe.web.controller.review;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import stable.horseCafe.config.security.LoginMember;
import stable.horseCafe.domain.member.Member;
import stable.horseCafe.service.review.ReviewService;
import stable.horseCafe.web.common.response.CommonResponse;
import stable.horseCafe.web.common.util.ResponseUtil;
import stable.horseCafe.web.dto.review.ReviewSaveReqDto;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/stable/v1/menu/{menuId}/review")
    public CommonResponse registerReview(@LoginMember Member member, @PathVariable Long menuId, @RequestBody ReviewSaveReqDto dto) {
        return ResponseUtil.getSingleResult("리뷰 등록", reviewService.registerReview(member, menuId, dto));
    }
}

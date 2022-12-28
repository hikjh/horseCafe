package stable.horseCafe.web.controller.review;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import stable.horseCafe.config.security.LoginMember;
import stable.horseCafe.domain.member.Member;
import stable.horseCafe.service.review.ReviewService;
import stable.horseCafe.web.common.response.CommonResponse;
import stable.horseCafe.web.common.util.ResponseUtil;
import stable.horseCafe.web.dto.review.ReviewSaveReqDto;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     *  리뷰 등록
     */
    @PostMapping("/stable/v1/menu/{menuId}/review")
    public CommonResponse registerReview(@LoginMember Member member, @PathVariable Long menuId, @RequestBody @Valid ReviewSaveReqDto dto) {
        return ResponseUtil.getSingleResult("리뷰 등록", reviewService.registerReview(member, menuId, dto));
    }

    /**
     *  내 리뷰 목록 조회
     */
    @GetMapping("/stable/v1/reviewList")
    public CommonResponse getMyReviewList(@LoginMember Member member) {
        return ResponseUtil.getSingleResult("내 리뷰 목록 조회", reviewService.getMyReviewList(member));
    }
}

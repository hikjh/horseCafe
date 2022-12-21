package stable.horseCafe.service.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stable.horseCafe.domain.member.Member;
import stable.horseCafe.domain.menu.Menu;
import stable.horseCafe.domain.menu.MenuRepository;
import stable.horseCafe.domain.review.Review;
import stable.horseCafe.domain.review.ReviewRepository;
import stable.horseCafe.web.common.exception.GlobalException;
import stable.horseCafe.web.common.response.code.ResponseCode;
import stable.horseCafe.web.dto.review.ReviewSaveReqDto;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MenuRepository menuRepository;

    @Transactional
    public Long registerReview(Member member, Long menuId, ReviewSaveReqDto dto) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> {
                    throw new GlobalException(ResponseCode.BAD_REQUEST, "해당 메뉴가 존재하지 않습니다.");
                });

        Review review = new Review(menu, member, dto.getContent());
        return reviewRepository.save(review).getId();
    }
}

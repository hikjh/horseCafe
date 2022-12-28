package stable.horseCafe.service.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stable.horseCafe.domain.member.Member;
import stable.horseCafe.domain.menu.Menu;
import stable.horseCafe.domain.menu.MenuRepository;
import stable.horseCafe.domain.order.OrderRepository;
import stable.horseCafe.domain.order.OrderStatus;
import stable.horseCafe.domain.review.Review;
import stable.horseCafe.domain.review.ReviewRepository;
import stable.horseCafe.web.common.exception.GlobalException;
import stable.horseCafe.web.common.response.code.ResponseCode;
import stable.horseCafe.web.dto.review.ReviewResDto;
import stable.horseCafe.web.dto.review.ReviewSaveReqDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;

    /**
     *  리뷰등록
     */
    @Transactional
    public Long registerReview(Member member, Long menuId, ReviewSaveReqDto dto) {

        // 메뉴 존재 여부 확인
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> {
                    throw new GlobalException(ResponseCode.BAD_REQUEST, "해당 메뉴가 존재하지 않습니다.");
                });

        // 해당 사용자가 주문한 메뉴만 리뷰등록 가능
        OrderStatus orderStatus = orderRepository.findOrderStatus(member.getId(), menuId);
        if (orderStatus == null || !orderStatus.equals(OrderStatus.ORDER)) {
            throw new GlobalException(ResponseCode.BAD_REQUEST, "주문상태의 상품만 리뷰 작성이 가능합니다.");
        }

        Review review = new Review(menu, member, dto.getContent());
        return reviewRepository.save(review).getId();
    }

    /**
     *  내 리뷰 목록 조회
     */
    public List<ReviewResDto> getMyReviewList(Member member) {
        return reviewRepository.findMyReviewList(member.getEmail());
    }
}
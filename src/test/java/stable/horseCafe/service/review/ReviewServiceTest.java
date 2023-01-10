package stable.horseCafe.service.review;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import stable.horseCafe.domain.member.Member;
import stable.horseCafe.domain.member.MemberRepository;
import stable.horseCafe.domain.menu.Menu;
import stable.horseCafe.domain.menu.MenuRepository;
import stable.horseCafe.domain.order.Order;
import stable.horseCafe.domain.order.OrderRepository;
import stable.horseCafe.domain.orderMenu.OrderMenu;
import stable.horseCafe.domain.review.Review;
import stable.horseCafe.domain.review.ReviewRepository;
import stable.horseCafe.web.common.exception.GlobalException;
import stable.horseCafe.web.dto.review.ReviewResDto;
import stable.horseCafe.web.dto.review.ReviewSaveReqDto;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static stable.horseCafe.domain.menu.MenuStatus.HOT;
import static stable.horseCafe.domain.menu.MenuType.COFFEE;

@SpringBootTest
@Transactional
class ReviewServiceTest {

    @Autowired
    ReviewService reviewService;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    OrderRepository orderRepository;

    @BeforeEach
    void clean() {
        reviewRepository.deleteAll();
    }

    @BeforeEach
    void singUp() {
        memberRepository.save(Member.builder()
                .name("홍길동")
                .email("hong@gmail.com")
                .password("1234")
                .build());
    }
    @BeforeEach
    void registerMenu() {
        menuRepository.save(Menu.builder()
                .name("아메리카노")
                .price(5000)
                .stockQuantity(20)
                .menuType(COFFEE)
                .menuStatus(HOT)
                .build());
    }

    @Test
    @DisplayName("리뷰 등록")
    void registerReview() {
        // given
        Member member = memberRepository.findAll().get(0);
        Menu menu = menuRepository.findAll().get(0);

        // 주문 메뉴
        List<OrderMenu> orderMenuList = new ArrayList<>();
        OrderMenu orderMenu = OrderMenu.builder()
                .menu(menu)
                .count(5)
                .orderMenuPrice(menu.getPrice())
                .build();
        orderMenuList.add(orderMenu);

        // 주문
        Order order = Order.builder()
                .member(member)
                .orderMenus(orderMenuList)
                .build();
        orderRepository.save(order);

        ReviewSaveReqDto dto = ReviewSaveReqDto.builder()
                .content("존맛탱")
                .build();

        // when
        Long reviewId = reviewService.registerReview(member, menu.getId(), dto);
        Review review = reviewRepository.findById(reviewId).get();

        // then
        assertNotNull(reviewId);
        assertEquals(dto.getContent(), review.getContent());
        assertEquals(member.getName(), review.getMember().getName());
        assertEquals(menu.getName(), review.getMenu().getName());
    }

    @Test
    @DisplayName("리뷰 등록 예외처리 - 주문상태의 상품만 리뷰 작성 가능")
    void registerReview_exception() {
        /**
         *  해당 상품을 주문하지 않고 리뷰를 작성하는 경우 - 예외처리 테스트
         */
        // given
        Member member = memberRepository.findAll().get(0);
        Menu menu = menuRepository.findAll().get(0);
        ReviewSaveReqDto dto = ReviewSaveReqDto.builder()
                .content("존맛탱")
                .build();

        // when
        GlobalException ge = assertThrows(GlobalException.class, () -> reviewService.registerReview(member, menu.getId(), dto));

        // then
        assertEquals("주문상태의 상품만 리뷰 작성이 가능합니다.", ge.getMessage());
    }

    @Test
    @DisplayName("내 리뷰 목록 조회")
    void getMyReviewList() {
        // given
        Member member = memberRepository.findAll().get(0);
        registerReview();

        // when
        List<ReviewResDto> myReviewList = reviewRepository.findMyReviewList(member.getEmail());

        // then
        assertNotNull(myReviewList.get(0).getReviewId());
        assertEquals(member.getName(), myReviewList.get(0).getMemberName());
    }
}
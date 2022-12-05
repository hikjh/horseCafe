package stable.horseCafe.web.dto.review;

import lombok.Builder;
import lombok.Getter;
import stable.horseCafe.domain.member.Member;
import stable.horseCafe.domain.menu.Menu;
import stable.horseCafe.domain.review.Review;

@Getter
public class ReviewSaveReqDto {
/*
    private Member member;
    private Long menuId;
    private String content;

    @Builder
    public ReviewSaveReqDto(Member member, Long menuId, String content) {
        this.member = member;
        this.menuId = menuId;
        this.content = content;
    }

    public Review toEntity() {
        return Review.builder()
                .member(member)
                .menu(menu)
                .content(content)
                .build();
    }
 */
}

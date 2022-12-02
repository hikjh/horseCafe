package stable.horseCafe.domain.review;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import stable.horseCafe.domain.BaseTimeEntity;
import stable.horseCafe.domain.member.Member;
import stable.horseCafe.domain.menu.Menu;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Getter
@NoArgsConstructor
@Entity
public class Review extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    private String content;

    @Builder
    public Review(Menu menu, Member member, String content) {
        this.menu = menu;
        this.member = member;
        this.content = content;
    }
}

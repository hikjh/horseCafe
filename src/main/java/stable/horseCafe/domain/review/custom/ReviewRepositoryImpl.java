package stable.horseCafe.domain.review.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import stable.horseCafe.web.dto.review.QReviewResDto;
import stable.horseCafe.web.dto.review.ReviewResDto;

import java.util.List;

import static stable.horseCafe.domain.review.QReview.*;

@RequiredArgsConstructor
public class ReviewRepositoryImpl implements CustomReviewRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ReviewResDto> findMyReviewList(String email) {

        return queryFactory
                .select(new QReviewResDto(
                        review.menu.id,
                        review.id,
                        review.member.name,
                        review.content
                ))
                .from(review)
                .where(
                        review.member.email.eq(email)
                )
                .fetch();
    }
}

package stable.horseCafe.domain.menu.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import stable.horseCafe.domain.menu.Menu;


import java.util.Optional;

import static stable.horseCafe.domain.menu.QMenu.*;
import static stable.horseCafe.domain.review.QReview.*;

@RequiredArgsConstructor
public class MenuRepositoryImpl implements CustomMenuRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Menu> findMenuAndReview(Long menuId) {

        Menu resultMenu = queryFactory
                .select(menu)
                .from(menu)
                .leftJoin(review).on(menu.id.eq(review.menu.id))
                .where(menu.id.eq(menuId))
                .fetchOne();

        return Optional.of(resultMenu);
    }
}

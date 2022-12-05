package stable.horseCafe.domain.menu.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MenuRepositoryImpl implements CustomMenuRepository {

    private final JPAQueryFactory queryFactory;

}

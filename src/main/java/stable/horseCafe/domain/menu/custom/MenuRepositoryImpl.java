package stable.horseCafe.domain.menu.custom;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import stable.horseCafe.domain.menu.MenuStatus;
import stable.horseCafe.domain.menu.MenuType;
import stable.horseCafe.web.dto.menu.MenuResDto;
import stable.horseCafe.web.dto.menu.MenuSearchCondition;
import stable.horseCafe.web.dto.menu.QMenuResDto;

import java.util.List;

import static stable.horseCafe.domain.menu.QMenu.menu;

@RequiredArgsConstructor
public class MenuRepositoryImpl implements CustomMenuRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MenuResDto> getMenuList(MenuSearchCondition cond) {

        return queryFactory
                .select(new QMenuResDto(
                        menu.id,
                        menu.name,
                        menu.price,
                        menu.stockQuantity,
                        menu.menuType,
                        menu.menuStatus
                ))
                .from(menu)
                .where(
                        menuNameContain(cond.getMenuName()),
                        menuTypeEq(cond.getMenuType()),     // COFFEE, TEA, ADE
                        menuStatus(cond.getMenuStatus())    // ICE, HOT
                )
                .limit(cond.getSize())
                .offset(cond.getOffset())
                .orderBy(menu.id.desc())
                .fetch();
    }

    private BooleanExpression menuNameContain(String menuName) {
        return menuName != null ? menu.name.contains(menuName) : null;
    }

    private BooleanExpression menuTypeEq(MenuType menuType) {
        return menuType != null ? menu.menuType.eq(menuType) : null;
    }

    private BooleanExpression menuStatus(MenuStatus menuStatus) {
        return menuStatus != null ? menu.menuStatus.eq(menuStatus) : null;
    }
}

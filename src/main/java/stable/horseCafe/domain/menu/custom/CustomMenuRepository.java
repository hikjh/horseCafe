package stable.horseCafe.domain.menu.custom;

import stable.horseCafe.domain.menu.Menu;
import stable.horseCafe.web.dto.menu.MenuResDto;

import java.util.List;
import java.util.Optional;

public interface CustomMenuRepository {
    Optional<Menu> findMenuAndReview(Long menuId);
}

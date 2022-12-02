package stable.horseCafe.domain.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import stable.horseCafe.domain.menu.custom.CustomMenuRepository;

public interface MenuRepository extends JpaRepository<Menu, Long>, CustomMenuRepository {
}

package stable.horseCafe.service.menu;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stable.horseCafe.domain.menu.MenuRepository;
import stable.horseCafe.web.dto.menu.MenuSaveReqDto;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    /**
     *  메뉴 등록
     */
    @Transactional
    public Long registerMenu(MenuSaveReqDto dto) {
        return menuRepository.save(dto.toEntity()).getId();
    }
}

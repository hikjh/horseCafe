package stable.horseCafe.domain.menu;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import stable.horseCafe.domain.member.Member;
import stable.horseCafe.domain.member.MemberRepository;
import stable.horseCafe.domain.review.Review;
import stable.horseCafe.domain.review.ReviewRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MenuRepositoryTest {

    @Autowired
    MenuRepository menuRepository;

    @Test
    public void save() {
        Menu menu = Menu.builder()
                        .name("아이스 아메리카노")
                        .price(4500)
                        .stockQuantity(50)
                        .build();

        Menu savedMenu = menuRepository.save(menu);

        assertNotNull(savedMenu.getId());
        assertEquals(savedMenu.getName(), menu.getName());
    }
}
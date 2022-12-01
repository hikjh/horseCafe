package stable.horseCafe.domain.member;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void save() {
        Member member = Member.builder()
                            .name("홍길동")
                            .email("hong@naver.com")
                            .password("1234")
                            .build();

        Member savedMember = memberRepository.save(member);

        assertNotNull(savedMember.getId());
        assertEquals(savedMember.getEmail(), member.getEmail());
    }
}
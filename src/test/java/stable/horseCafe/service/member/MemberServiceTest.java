package stable.horseCafe.service.member;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import stable.horseCafe.domain.member.Member;
import stable.horseCafe.domain.member.MemberRepository;
import stable.horseCafe.web.dto.member.MemberSaveReqDto;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    void signUp() {
        MemberSaveReqDto dto = MemberSaveReqDto.builder()
                                                .name("userA")
                                                .email("AA@naver.com")
                                                .password("1234")
                                                .build();

        Long savedId = memberService.signUp(dto);
        Member member = memberRepository.findById(savedId).get();

        assertNotNull(savedId);
        assertEquals(member.getEmail(), dto.getEmail());
    }
}
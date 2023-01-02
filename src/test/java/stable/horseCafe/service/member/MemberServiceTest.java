package stable.horseCafe.service.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import stable.horseCafe.domain.member.Member;
import stable.horseCafe.domain.member.MemberRepository;
import stable.horseCafe.web.dto.member.MemberLoginReqDto;
import stable.horseCafe.web.dto.member.MemberLoginResDto;
import stable.horseCafe.web.dto.member.MemberSaveReqDto;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 및 비밀번호 암호화")
    void signUp() {
        // given
        MemberSaveReqDto dto = MemberSaveReqDto.builder()
                                                .name("userA")
                                                .email("AA@naver.com")
                                                .password("1234")
                                                .build();

        // when
        Long savedId = memberService.signUp(dto);
        Member member = memberRepository.findById(savedId).get();

        // then
        assertNotNull(savedId);
        assertEquals(dto.getEmail(), member.getEmail());
        assertNotEquals("1234", member.getPassword());
    }

    @Test
    @DisplayName("로그인 및 AccessToken 발급")
    void login() {
        // given
        signUp();
        MemberLoginReqDto reqDto = MemberLoginReqDto.builder()
                .email("AA@naver.com")
                .password("1234")
                .build();

        // when
        MemberLoginResDto resDto = memberService.login(reqDto);

        //then
        assertNotNull(resDto.getAccessToken());
    }
}
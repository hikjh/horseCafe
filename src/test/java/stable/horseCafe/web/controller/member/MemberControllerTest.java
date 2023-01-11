package stable.horseCafe.web.controller.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import stable.horseCafe.domain.member.Member;
import stable.horseCafe.domain.member.MemberRepository;
import stable.horseCafe.web.dto.member.MemberLoginReqDto;
import stable.horseCafe.web.dto.member.MemberSaveReqDto;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MemberControllerTest {

    /*
         @WebMvcTest
         @AutoConfigureMockMvc
         - 차이점
     */
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입")
    void singUp() throws Exception {
        // given
        MemberSaveReqDto reqDto = MemberSaveReqDto.builder()
                .name("홍길동")
                .email("hong@gmail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(reqDto);

        // expected
        mockMvc.perform(post("/stable/v1/signUp")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.message").value("회원가입"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 필드값 유효성 검사")
    void signUp_validation() throws Exception {
        // given
        MemberSaveReqDto reqDto = MemberSaveReqDto.builder()
                .name("홍길동")
                .email("")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(reqDto);

        // expected
        mockMvc.perform(post("/stable/v1/signUp")
                .contentType(APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.message").value("이메일은 필수값입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("로그인")
    @Transactional
    void login() throws Exception {
        // given
        saveMember();
        MemberLoginReqDto reqDto = MemberLoginReqDto.builder()
                .email("hong@gmail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(reqDto);

        // expected
        mockMvc.perform(post("/stable/v1/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.message").value("로그인"))
                .andDo(print());
    }

    private void saveMember() {
        Member member = Member.builder()
                .name("홍길동")
                .email("hong@gmail.com")
                .password(passwordEncoder.encode("1234"))
                .build();
        memberRepository.save(member);
    }
}
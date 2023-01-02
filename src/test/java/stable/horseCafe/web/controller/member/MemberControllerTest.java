package stable.horseCafe.web.controller.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import stable.horseCafe.domain.member.MemberRepository;
import stable.horseCafe.web.dto.member.MemberLoginReqDto;
import stable.horseCafe.web.dto.member.MemberSaveReqDto;

import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


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

    @BeforeEach
    void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입")
    void singUp() throws Exception {
        MemberSaveReqDto reqDto = MemberSaveReqDto.builder()
                .name("홍길동")
                .email("hong@naver.com")
                .password("1234").build();

        String json = objectMapper.writeValueAsString(reqDto);
        mockMvc.perform(post("/stable/v1/signUp")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.message").value("회원가입"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 필드값 유효성 검사")
    void signUp_validation() throws Exception {
        MemberSaveReqDto reqDto = MemberSaveReqDto.builder()
                .name("홍길동")
                .email("")
                .password("1234").build();

        String json = objectMapper.writeValueAsString(reqDto);
        mockMvc.perform(post("/stable/v1/signUp")
                .contentType(APPLICATION_JSON)
                .content(json))
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.message").value("이메일은 필수값입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("로그인")
    void login() throws Exception {
        singUp();
        MemberLoginReqDto reqDto = MemberLoginReqDto.builder()
                .email("hong@naver.com")
                .password("1234").build();

        String json = objectMapper.writeValueAsString(reqDto);
        mockMvc.perform(post("/stable/v1/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.message").value("로그인"))
                .andDo(print());
    }
}
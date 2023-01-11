package stable.horseCafe.web.controller.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import stable.horseCafe.domain.member.Member;
import stable.horseCafe.domain.member.MemberRepository;
import stable.horseCafe.web.dto.member.MemberLoginReqDto;
import stable.horseCafe.web.dto.member.MemberSaveReqDto;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "stable.horseCafe.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
@Transactional
public class MemberControllerDocTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입")
    void signUp() throws Exception {
        // given
        MemberSaveReqDto dto = MemberSaveReqDto.builder()
                .name("홍길동")
                .email("hong@gmail.com")
                .password("1234")
                .build();
        String json = objectMapper.writeValueAsString(dto);

        // expected
        mockMvc.perform(post("/stable/v1/signUp")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.message").value("회원가입"))
                .andDo(print())
                .andDo(document("member-signUp",
                        requestFields(
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("password").description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 코드"),
                                fieldWithPath("message").description("응답 메세지"),
                                fieldWithPath("data").description("회원 아이디")
                        )
                ));
    }

    @Test
    @DisplayName("로그인")
    void login() throws Exception {
        // given
        saveMember();   //회원가입
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
                .andDo(print())
                .andDo(document("member-login",
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("password").description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("status").description("응답 코드"),
                                fieldWithPath("message").description("응답 메세지"),
                                fieldWithPath("data.accessToken").description("JWT 토큰 정보")
                        )
                ));
    }
    void saveMember() {
        Member member = Member.builder()
                .name("홍길동")
                .email("hong@gmail.com")
                .password(passwordEncoder.encode("1234"))
                .build();
        memberRepository.save(member);
    }
}

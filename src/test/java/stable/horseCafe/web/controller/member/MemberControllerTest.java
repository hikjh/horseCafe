package stable.horseCafe.web.controller.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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

    @Test
    @DisplayName("회원가입")
    void 회원가입() throws Exception {
        /*
            [
               {
                    "id": 1,
                    "username": "Kyeongho Yoo",
                    "address": {
                        "city": "서울",
                        "street": "오솔길",
                        "zipcode": "123-123"
                    }
                }
            ]
             String expectByUsername = "$.[?(@.username == '%s')]";
             String addressByCity = "$..address[?(@.city == '%s')]";
         */
        String reqData = "{\n" +
                "    \"name\" : \"홍길동\",\n" +
                "    \"email\" : \"hong@naver.com\",\n" +
                "    \"password\" : \"1234\"\n" +
                "}";

        mockMvc.perform(post("/stable/v1/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqData))
                .andExpect(jsonPath("$.message").value("회원가입"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("회원가입 필드값 유효성 검사")
    void 회원가입_필드값_유효성검사() throws Exception {

        String reqData = "{\n" +
                "    \"name\" : \"홍길동\",\n" +
                "    \"email\" : \"\",\n" +
                "    \"password\" : \"1234\"\n" +
                "}";

        mockMvc.perform(post("/stable/v1/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reqData))
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.message").value("이메일은 필수값입니다."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void 로그인() throws Exception {
        회원가입();
        String reqData = "{\n" +
                "    \"email\" : \"hong@naver.com\",\n" +
                "    \"password\" : \"1234\"\n" +
                "}";

        mockMvc.perform(post("/stable/v1/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reqData))
                .andExpect(jsonPath("$.message").value("로그인"))
                .andDo(MockMvcResultHandlers.print());
    }
}
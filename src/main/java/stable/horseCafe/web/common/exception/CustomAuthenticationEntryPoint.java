package stable.horseCafe.web.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import stable.horseCafe.web.common.response.CommonResponse;
import stable.horseCafe.web.common.response.code.ResponseCode;
import stable.horseCafe.web.common.util.ResponseUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Getter
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        String msg = (String) request.getAttribute("exceptionMessage");

        if (msg == null) {
            msg = "토큰 정보가 없습니다.";
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        CommonResponse commonResponse = ResponseUtil.failResponse(ResponseCode.BAD_REQUEST, msg);
        response.getWriter().print(objectMapper.writeValueAsString(commonResponse));
    }
}

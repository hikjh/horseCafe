package stable.horseCafe.web.controller.member;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import stable.horseCafe.service.member.MemberService;
import stable.horseCafe.web.common.exception.GlobalException;
import stable.horseCafe.web.common.response.CommonResponse;
import stable.horseCafe.web.common.response.code.ResponseCode;
import stable.horseCafe.web.common.util.ResponseUtil;
import stable.horseCafe.web.dto.member.MemberLoginReqDto;
import stable.horseCafe.web.dto.member.MemberSaveReqDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     *  회원 가입
     */
    @PostMapping("/stable/v1/signUp")
    public CommonResponse singUp(@RequestBody @Valid MemberSaveReqDto dto, BindingResult result) {
        if (result.hasErrors()) {
            FieldError fieldError = result.getFieldError();
            throw new GlobalException(ResponseCode.BAD_REQUEST, fieldError.getDefaultMessage());
        }
        return ResponseUtil.getSingleResult("회원가입", memberService.signUp(dto));
    }

    /**
     *  로그인
     */
    @PostMapping("/stable/v1/login")
    public CommonResponse login(@RequestBody MemberLoginReqDto dto) {
        return ResponseUtil.getSingleResult("로그인", memberService.login(dto));
    }
}

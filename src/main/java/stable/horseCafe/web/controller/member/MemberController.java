package stable.horseCafe.web.controller.member;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import stable.horseCafe.service.member.MemberService;
import stable.horseCafe.web.common.response.CommonResponse;
import stable.horseCafe.web.common.util.ResponseUtil;
import stable.horseCafe.web.dto.member.MemberLoginReqDto;
import stable.horseCafe.web.dto.member.MemberSaveReqDto;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     *  회원 가입
     */
    @PostMapping("/stable/v1/signUp")
    public CommonResponse singUp(@RequestBody @Valid MemberSaveReqDto dto) {
        return ResponseUtil.getSingleResult("회원가입", memberService.signUp(dto));
    }

    /**
     *  로그인
     */
    @PostMapping("/stable/v1/login")
    public CommonResponse login(@RequestBody @Valid MemberLoginReqDto dto) {
        return ResponseUtil.getSingleResult("로그인", memberService.login(dto));
    }
}

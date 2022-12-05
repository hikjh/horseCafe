package stable.horseCafe.web.controller.member;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import stable.horseCafe.service.member.MemberService;
import stable.horseCafe.web.common.response.CommonResponse;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    public CommonResponse singUp() {

    }
}

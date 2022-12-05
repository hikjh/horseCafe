package stable.horseCafe.web.dto.member;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberLoginReqDto {

    private String email;
    private String password;

    @Builder
    public MemberLoginReqDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}

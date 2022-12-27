package stable.horseCafe.web.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor
@ToString
public class MemberLoginReqDto {

    @NotBlank(message = "이메일은 필수값입니다.")
    private String email;
    @NotBlank(message = "비밀번호는 필수값입니다.")
    private String password;

    @Builder
    public MemberLoginReqDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}

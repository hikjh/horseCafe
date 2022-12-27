package stable.horseCafe.web.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import stable.horseCafe.domain.member.Member;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@ToString
public class MemberSaveReqDto {

    @NotBlank(message = "이름은 필수값입니다.")
    private String name;
    @NotBlank(message = "이메일은 필수값입니다.")
    private String email;
    @NotBlank(message = "비밀번호는 필수값입니다.")
    private String password;

    @Builder
    public MemberSaveReqDto(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Member toEntity() {
        return Member.builder()
                .name(name)
                .email(email)
                .password(password)
                .build();
    }
}

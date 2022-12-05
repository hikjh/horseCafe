package stable.horseCafe.web.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import stable.horseCafe.domain.member.Member;

@Getter
@NoArgsConstructor
public class MemberSaveReqDto {

    private String name;
    private String email;
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

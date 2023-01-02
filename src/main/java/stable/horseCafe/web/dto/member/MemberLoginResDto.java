package stable.horseCafe.web.dto.member;

import lombok.Getter;

@Getter
public class MemberLoginResDto {

    private String accessToken;

    public MemberLoginResDto(String accessToken) {
        this.accessToken = accessToken;
    }
}

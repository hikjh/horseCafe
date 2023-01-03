package stable.horseCafe.web.dto.member;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberLoginResDto {

    private String accessToken;

    @Builder
    public MemberLoginResDto(String accessToken) {
        this.accessToken = accessToken;
    }
}

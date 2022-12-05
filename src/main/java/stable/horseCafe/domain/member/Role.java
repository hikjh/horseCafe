package stable.horseCafe.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    MEMBER("ROLE_MEMBER", "사용자"),
    ADMIN("ROLE_ADMIN", "관리자");

    private final String key;
    private final String value;
}

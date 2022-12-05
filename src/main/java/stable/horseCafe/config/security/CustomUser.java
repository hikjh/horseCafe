package stable.horseCafe.config.security;

import lombok.Getter;
import org.springframework.security.core.userdetails.User;
import stable.horseCafe.domain.member.Member;

@Getter
public class CustomUser extends User {

    private Member member;

    public CustomUser(Member member) {
        super(member.getUsername(), member.getPassword(), member.getAuthorities());
        this.member = member;
    }
}

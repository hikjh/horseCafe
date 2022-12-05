package stable.horseCafe.config.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import stable.horseCafe.config.security.CustomUserDetailService;
import stable.horseCafe.domain.member.Member;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final CustomUserDetailService customUserDetailService;
    public static final Long ACCESS_TOKEN_VALID_TIME = 30 * 60 * 1000L;//30분

    @Value("${spring.jwt.secret-key}")
    private String SECRET_KEY;
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    @PostConstruct
    protected void init() {
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
    }

    public String createToken(Member member) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setSubject(member.getEmail())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME))
                .claim("ROLE", member.getRole().getKey())
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String getToken(HttpServletRequest req) {
        String bearerToken = req.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    public boolean validateAccessToken(HttpServletRequest req, String token) {
        String msg = "exceptionMessage";
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
            return true;
        } catch (SignatureException e) {
            req.setAttribute(msg, "서명이 유효하지 않은 토큰입니다.");
        } catch (MalformedJwtException e) {
            req.setAttribute(msg, "유효하지 않은 토큰입니다.");
        } catch (ExpiredJwtException e) {
            req.setAttribute(msg, "만료된 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            req.setAttribute(msg, "지원되지 않는 토큰입니다.");
        } catch (IllegalArgumentException e) {
            req.setAttribute(msg, "압축이 잘못된 토큰입니다.");
        }
        return false;
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = customUserDetailService.loadUserByUsername(this.getUserId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getUserId(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }
}

package stable.horseCafe.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stable.horseCafe.config.jwt.JwtProvider;
import stable.horseCafe.domain.member.Member;
import stable.horseCafe.domain.member.MemberRepository;
import stable.horseCafe.web.common.exception.GlobalException;
import stable.horseCafe.web.common.response.code.ResponseCode;
import stable.horseCafe.web.dto.member.MemberLoginReqDto;
import stable.horseCafe.web.dto.member.MemberLoginResDto;
import stable.horseCafe.web.dto.member.MemberSaveReqDto;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;


    /**
     *  회원가입
     */
    @Transactional
    public Long signUp(MemberSaveReqDto dto) {

        memberRepository.findByEmail(dto.getEmail())
                .ifPresent(member -> {
                    throw new GlobalException(ResponseCode.BAD_REQUEST, "존재하는 회원입니다.");
                });

        // 비밀번호 암호화
        dto = getEncodePassword(dto);
        return memberRepository.save(dto.toEntity()).getId();
    }

    /**
     *  비밀번호 암호화
     */
    private MemberSaveReqDto getEncodePassword(MemberSaveReqDto dto) {
        String encodePassword = passwordEncoder.encode(dto.getPassword());
        return new MemberSaveReqDto(dto.getName(), dto.getEmail(), encodePassword);
    }

    /**
     *  로그인
     */
    public MemberLoginResDto login(MemberLoginReqDto dto) {

        Member member = memberRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> {
                    throw new GlobalException(ResponseCode.BAD_REQUEST, "아이디 혹은 비밀번호를 확인하세요.");
                });

        // 비밀번호 검증
        if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            throw new GlobalException(ResponseCode.BAD_REQUEST, "아이디 혹은 비밀번호를 확인하세요.");
        }

        // AccessToken 생성
        String accessToken = jwtProvider.createToken(member);
        return new MemberLoginResDto(accessToken);
    }
}

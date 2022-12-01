package stable.horseCafe.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stable.horseCafe.domain.member.MemberRepository;
import stable.horseCafe.web.common.exception.GlobalException;
import stable.horseCafe.web.common.response.code.ResponseCode;
import stable.horseCafe.web.dto.member.MemberSaveReqDto;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     *  회원가입
     */
    @Transactional
    public Long signUp(MemberSaveReqDto dto) {

        memberRepository.findByEmail(dto.getEmail())
                .ifPresent(member -> {
                    throw new GlobalException(ResponseCode.BAD_REQUEST, "존재하는 회원입니다.");
                });

        return memberRepository.save(dto.toEntity()).getId();
    }
}

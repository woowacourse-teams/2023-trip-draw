package dev.tripdraw.application;

import static dev.tripdraw.exception.ExceptionCode.ALREADY_HAS_NICKNAME;
import static dev.tripdraw.exception.ExceptionCode.HAS_NO_MEMBER;

import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.member.MemberRepository;
import dev.tripdraw.dto.LoginUser;
import dev.tripdraw.dto.request.MemberCreateRequest;
import dev.tripdraw.dto.response.MemberCreateResponse;
import dev.tripdraw.exception.AuthException;
import dev.tripdraw.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Member validateLoginUser(LoginUser loginUser) {
        return memberRepository.findByNickname(loginUser.nickname())
                .orElseThrow(() -> new AuthException(HAS_NO_MEMBER));
    }

    public MemberCreateResponse register(MemberCreateRequest memberCreateRequest) {
        String nickname = memberCreateRequest.nickname();
        if (memberRepository.existsByNickname(nickname)) {
            throw new BadRequestException(ALREADY_HAS_NICKNAME);
        }

        Member member = memberRepository.save(new Member(nickname));
        return MemberCreateResponse.from(member);
    }
}

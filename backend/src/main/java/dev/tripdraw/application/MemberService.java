package dev.tripdraw.application;

import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.member.MemberRepository;
import dev.tripdraw.dto.LoginUser;
import dev.tripdraw.dto.request.MemberCreateRequest;
import dev.tripdraw.exception.BadRequestException;
import dev.tripdraw.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Member validateLoginUser(LoginUser loginUser) {
        Member member = findByNickname(loginUser.nickname());

        if (member == null) {
            throw new BadRequestException(ExceptionCode.HAS_NO_MEMBER);
        }

        return member;
    }

    public Long register(MemberCreateRequest memberCreateRequest) {
        String nickname = memberCreateRequest.nickname();
        Member byNickname = findByNickname(nickname);

        if (byNickname != null) {
            throw new BadRequestException(ExceptionCode.ALREADY_HAS_NICKNAME);
        }

        Member member = memberRepository.save(new Member(nickname));
        return member.getId();
    }

    private Member findByNickname(String nickname) {
        return memberRepository.findByNickname(nickname);
    }
}

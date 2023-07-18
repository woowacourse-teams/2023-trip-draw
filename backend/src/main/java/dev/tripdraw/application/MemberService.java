package dev.tripdraw.application;

import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.member.MemberRepository;
import dev.tripdraw.dto.LoginUser;
import dev.tripdraw.dto.request.MemberCreateRequest;
import dev.tripdraw.exception.AuthException;
import dev.tripdraw.exception.BadRequestException;
import dev.tripdraw.exception.ExceptionCode;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Member validateLoginUser(LoginUser loginUser) {
        Optional<Member> member = memberRepository.findByNickname(loginUser.nickname());

        if (member.isEmpty()) {
            throw new AuthException(ExceptionCode.HAS_NO_MEMBER);
        }

        return member.get();
    }

    public Long register(MemberCreateRequest memberCreateRequest) {
        String nickname = memberCreateRequest.nickname();
        Optional<Member> byNickname = memberRepository.findByNickname(nickname);
        ;

        if (byNickname.isPresent()) {
            throw new BadRequestException(ExceptionCode.ALREADY_HAS_NICKNAME);
        }

        Member member = memberRepository.save(new Member(nickname));
        return member.getId();
    }
}

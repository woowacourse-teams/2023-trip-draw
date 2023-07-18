package dev.tripdraw.application;

import static dev.tripdraw.exception.member.MemberExceptionType.MEMBER_NOT_FOUND;

import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.member.MemberRepository;
import dev.tripdraw.dto.LoginUser;
import dev.tripdraw.dto.request.MemberCreateRequest;
import dev.tripdraw.dto.response.MemberCreateResponse;
import dev.tripdraw.exception.member.MemberException;
import jakarta.validation.Valid;
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
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }

    public MemberCreateResponse register(@Valid MemberCreateRequest memberCreateRequest) {
        String nickname = memberCreateRequest.nickname();

        Member member = memberRepository.save(new Member(nickname));
        return MemberCreateResponse.from(member);
    }
}

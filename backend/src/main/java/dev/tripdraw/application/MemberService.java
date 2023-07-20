package dev.tripdraw.application;

import static dev.tripdraw.exception.member.MemberExceptionType.NICKNAME_CONFLICT;

import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.member.MemberRepository;
import dev.tripdraw.dto.member.MemberCreateRequest;
import dev.tripdraw.dto.member.MemberResponse;
import dev.tripdraw.exception.member.MemberException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public boolean existsByNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    public MemberResponse register(MemberCreateRequest memberCreateRequest) {
        String nickname = memberCreateRequest.nickname();
        if (existsByNickname(nickname)) {
            throw new MemberException(NICKNAME_CONFLICT);
        }

        Member member = memberRepository.save(new Member(nickname));
        return MemberResponse.from(member);
    }
}
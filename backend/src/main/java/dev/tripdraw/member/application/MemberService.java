package dev.tripdraw.member.application;

import dev.tripdraw.common.auth.LoginUser;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberDeleteEvent;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.member.dto.MemberSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher publisher;

    @Transactional(readOnly = true)
    public boolean existsById(Long memberId) {
        return memberRepository.existsById(memberId);
    }

    @Transactional(readOnly = true)
    public MemberSearchResponse find(LoginUser loginUser) {
        Member member = memberRepository.getById(loginUser.memberId());
        return MemberSearchResponse.from(member);
    }

    public void delete(LoginUser loginUser) {
        Long memberId = loginUser.memberId();
        publisher.publishEvent(new MemberDeleteEvent(memberId));
        memberRepository.deleteById(memberId);
    }
}

package dev.tripdraw.application;

import static dev.tripdraw.exception.member.MemberExceptionType.MEMBER_NOT_FOUND;

import dev.tripdraw.application.oauth.AuthTokenManager;
import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.member.MemberRepository;
import dev.tripdraw.dto.member.MemberSearchResponse;
import dev.tripdraw.exception.member.MemberException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthTokenManager authTokenManager;

    public MemberService(MemberRepository memberRepository, AuthTokenManager authTokenManager) {
        this.memberRepository = memberRepository;
        this.authTokenManager = authTokenManager;
    }

    @Transactional(readOnly = true)
    public boolean existsById(Long memberId) {
        return memberRepository.existsById(memberId);
    }

    @Transactional(readOnly = true)
    public MemberSearchResponse findByCode(String code) {
        Long memberId = authTokenManager.extractMemberId(code);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
        return MemberSearchResponse.from(member);
    }

    public void deleteByCode(String code) {
        Long memberId = authTokenManager.extractMemberId(code);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
        member.delete();
    }
}

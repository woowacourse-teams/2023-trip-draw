package dev.tripdraw.member.application;

import dev.tripdraw.auth.application.AuthTokenManager;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.post.domain.PostRepository;
import dev.tripdraw.trip.domain.TripRepository;
import dev.tripdraw.member.dto.MemberSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final TripRepository tripRepository;
    private final PostRepository postRepository;
    private final AuthTokenManager authTokenManager;

    @Transactional(readOnly = true)
    public boolean existsById(Long memberId) {
        return memberRepository.existsById(memberId);
    }

    @Transactional(readOnly = true)
    public MemberSearchResponse findByCode(String code) {
        Long memberId = authTokenManager.extractMemberId(code);
        Member member = memberRepository.getById(memberId);
        return MemberSearchResponse.from(member);
    }

    public void deleteByCode(String code) {
        Long memberId = authTokenManager.extractMemberId(code);
        postRepository.deleteByMemberId(memberId);
        tripRepository.deleteByMemberId(memberId);
        memberRepository.deleteById(memberId);
    }
}

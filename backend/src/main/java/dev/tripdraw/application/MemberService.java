package dev.tripdraw.application;

import dev.tripdraw.application.oauth.AuthTokenManager;
import dev.tripdraw.domain.member.Member;
import dev.tripdraw.domain.member.MemberRepository;
import dev.tripdraw.domain.post.PostRepository;
import dev.tripdraw.domain.trip.TripRepository;
import dev.tripdraw.dto.member.MemberSearchResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final TripRepository tripRepository;
    private final PostRepository postRepository;
    private final AuthTokenManager authTokenManager;

    public MemberService(
            MemberRepository memberRepository,
            TripRepository tripRepository,
            PostRepository postRepository,
            AuthTokenManager authTokenManager
    ) {
        this.memberRepository = memberRepository;
        this.tripRepository = tripRepository;
        this.postRepository = postRepository;
        this.authTokenManager = authTokenManager;
    }

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

package dev.tripdraw.auth.application;

import static dev.tripdraw.member.exception.MemberExceptionType.DUPLICATE_NICKNAME;
import static dev.tripdraw.member.exception.MemberExceptionType.MEMBER_NOT_FOUND;

import dev.tripdraw.auth.dto.OauthInfo;
import dev.tripdraw.member.domain.Member;
import dev.tripdraw.member.domain.MemberRepository;
import dev.tripdraw.member.exception.MemberException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class AuthService {

    private final MemberRepository memberRepository;

    public Optional<Member> login(OauthInfo oauthInfo) {
        return memberRepository.findByOauthIdAndOauthType(oauthInfo.oauthId(), oauthInfo.oauthType())
                .or(() -> {
                    memberRepository.save(Member.of(oauthInfo.oauthId(), oauthInfo.oauthType()));
                    return Optional.empty();
                });
    }

    public Optional<Member> register(OauthInfo oauthInfo, String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new MemberException(DUPLICATE_NICKNAME);
        }

        Member member = memberRepository.findByOauthIdAndOauthType(oauthInfo.oauthId(), oauthInfo.oauthType())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
        member.changeNickname(nickname);
        return Optional.of(member);
    }
}

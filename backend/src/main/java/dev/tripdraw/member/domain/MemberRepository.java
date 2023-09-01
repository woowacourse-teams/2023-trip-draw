package dev.tripdraw.member.domain;

import static dev.tripdraw.member.exception.MemberExceptionType.MEMBER_NOT_FOUND;

import dev.tripdraw.auth.domain.OauthType;
import dev.tripdraw.member.exception.MemberException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByOauthIdAndOauthType(String oauthId, OauthType oauthType);

    boolean existsByNickname(String nickname);

    default Member getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }
}

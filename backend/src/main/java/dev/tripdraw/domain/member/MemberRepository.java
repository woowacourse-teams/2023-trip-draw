package dev.tripdraw.domain.member;

import static dev.tripdraw.exception.member.MemberExceptionType.MEMBER_NOT_FOUND;

import dev.tripdraw.domain.oauth.OauthType;
import dev.tripdraw.exception.member.MemberException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByOauthIdAndOauthType(String oauthId, OauthType oauthType);

    default Member getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }
}

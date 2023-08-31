package dev.tripdraw.domain.member;

import static dev.tripdraw.exception.member.MemberExceptionType.MEMBER_NOT_FOUND;

import dev.tripdraw.domain.oauth.OauthType;
import dev.tripdraw.exception.member.MemberException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByOauthIdAndOauthType(String oauthId, OauthType oauthType);

    boolean existsByNickname(String nickname);

    @NonNull
    default Member getById(@NonNull Long id) {
        return findById(id)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }
}

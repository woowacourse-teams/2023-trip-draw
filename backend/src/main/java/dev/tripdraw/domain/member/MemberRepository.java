package dev.tripdraw.domain.member;

import dev.tripdraw.domain.oauth.OauthType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByOauthIdAndOauthType(String oauthId, OauthType oauthType);

    boolean existsByNickname(String nickname);
}

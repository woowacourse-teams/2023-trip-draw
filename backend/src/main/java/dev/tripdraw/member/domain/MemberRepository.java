package dev.tripdraw.member.domain;

import static dev.tripdraw.member.exception.MemberExceptionType.MEMBER_NOT_FOUND;

import dev.tripdraw.common.auth.OauthType;
import dev.tripdraw.member.exception.MemberException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByOauthIdAndOauthType(String oauthId, OauthType oauthType);

    boolean existsByNickname(String nickname);

    @Query("SELECT m.nickname FROM Member m WHERE m.id = :id")
    String getNicknameById(@Param("id") Long id);

    default Member getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }
}

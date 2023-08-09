package dev.tripdraw.domain.member;

import dev.tripdraw.domain.common.BaseEntity;
import dev.tripdraw.domain.oauth.OauthType;
import dev.tripdraw.exception.member.MemberException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.Where;

import static dev.tripdraw.exception.member.MemberExceptionType.MEMBER_NOT_FOUND;
import static jakarta.persistence.GenerationType.IDENTITY;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Where(clause = "is_deleted = FALSE")
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String nickname;

    @Column(nullable = false)
    private String oauthId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OauthType oauthType;

    @Column(nullable = false)
    private Boolean isDeleted = FALSE;

    protected Member() {
    }

    public Member(Long id, String nickname, String oauthId, OauthType oauthType) {
        this.id = id;
        this.nickname = nickname;
        this.oauthId = oauthId;
        this.oauthType = oauthType;
    }

    public Member(String nickname, String oauthId, OauthType oauthType) {
        this(null, nickname, oauthId, oauthType);
    }

    public static Member of(String oauthId, OauthType oauthType) {
        return new Member(null, null, oauthId, oauthType);
    }

    public void delete() {
        validateNotDeleted();
        this.isDeleted = TRUE;
    }

    private void validateNotDeleted() {
        if (isDeleted == TRUE) {
            throw new MemberException(MEMBER_NOT_FOUND);
        }
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public Long id() {
        return id;
    }

    public String nickname() {
        return nickname;
    }

    public String oauthId() {
        return oauthId;
    }

    public OauthType oauthType() {
        return oauthType;
    }

    public Boolean isDeleted() {
        return isDeleted;
    }
}

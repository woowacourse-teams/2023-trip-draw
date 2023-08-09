package dev.tripdraw.domain.member;

import static jakarta.persistence.GenerationType.IDENTITY;

import dev.tripdraw.domain.common.BaseEntity;
import dev.tripdraw.domain.oauth.OauthType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

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

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public Long id() {
        return id;
    }

    public String nickname() {
        return nickname;
    }
}

package dev.tripdraw.member.domain;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import dev.tripdraw.auth.domain.OauthType;
import dev.tripdraw.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
@NoArgsConstructor(access = PROTECTED)
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

    public Member(String nickname, String oauthId, OauthType oauthType) {
        this(null, nickname, oauthId, oauthType);
    }

    public Member(Long id, String nickname, String oauthId, OauthType oauthType) {
        this.id = id;
        this.nickname = nickname;
        this.oauthId = oauthId;
        this.oauthType = oauthType;
    }

    public static Member of(String oauthId, OauthType oauthType) {
        return new Member(null, null, oauthId, oauthType);
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }
}

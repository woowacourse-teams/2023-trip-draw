package dev.tripdraw.auth.domain;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import dev.tripdraw.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class RefreshToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "refresh_token_id")
    private Long id;

    private Long memberId;

    private String token;

    public RefreshToken(Long memberId, String token) {
        this(null, memberId, token);
    }

    public RefreshToken(Long id, Long memberId, String token) {
        this.id = id;
        this.memberId = memberId;
        this.token = token;
    }
}

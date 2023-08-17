package dev.tripdraw.domain.member;

import static jakarta.persistence.GenerationType.IDENTITY;

import dev.tripdraw.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String nickname;

    protected Member() {
    }

    public Member(String nickname) {
        this(null, nickname);
    }

    public Member(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    public Long id() {
        return id;
    }

    public String nickname() {
        return nickname;
    }
}

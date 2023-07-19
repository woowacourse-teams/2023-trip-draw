package dev.tripdraw.domain.member;

import dev.tripdraw.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Member extends BaseEntity {

    private static final int NICKNAME_MAX_LENGTH = 10;

    @Column(nullable = false, unique = true)
    private String nickname;

    protected Member() {
    }

    public Member(String nickname) {
        this.nickname = nickname;
    }

    public String nickname() {
        return nickname;
    }
}

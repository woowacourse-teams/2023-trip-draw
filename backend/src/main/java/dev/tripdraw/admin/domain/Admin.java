package dev.tripdraw.admin.domain;

import static dev.tripdraw.admin.exception.AdminExceptionType.AUTH_FAIL;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import dev.tripdraw.admin.exception.AdminException;
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
public class Admin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "admin_id")
    private Long id;

    private String nickname;

    private String password;

    private Long failCount;

    public Admin(String nickname, String password) {
        this(null, nickname, password, 0L);
    }

    public Admin(Long id, String nickname, String password, Long failCount) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
        this.failCount = failCount;
    }

    public void validatePassword(String password) {
        if (!this.password.equals(password)) {
            failCount++;
            throw new AdminException(AUTH_FAIL);
        }
    }
}

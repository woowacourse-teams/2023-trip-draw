package dev.tripdraw.admin.domain;

import static dev.tripdraw.admin.exception.AdminExceptionType.ADMIN_AUTH_FAIL;
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

    private static final int MIN_FAIL_COUNT = 5;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "admin_id")
    private Long id;

    private String email;

    private String password;

    private Long failCount;

    public Admin(String email, String password) {
        this(null, email, password, 0L);
    }

    public Admin(Long id, String email, String password, Long failCount) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.failCount = failCount;
    }

    public void increaseFailCount() {
        failCount++;
    }

    public void resetFailCount() {
        failCount = 0L;
    }

    public void validateFailCount() {
        if (failCount >= MIN_FAIL_COUNT) {
            throw new AdminException(ADMIN_AUTH_FAIL);
        }
    }
}

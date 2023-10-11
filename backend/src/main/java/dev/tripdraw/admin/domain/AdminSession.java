package dev.tripdraw.admin.domain;

import static dev.tripdraw.admin.exception.AdminExceptionType.ADMIN_AUTH_FAIL;
import static jakarta.persistence.GenerationType.IDENTITY;

import dev.tripdraw.admin.exception.AdminException;
import dev.tripdraw.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Getter
@Entity
public class AdminSession extends BaseEntity {

    private static final int SESSION_SECONDS = 3600;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "admin_session_id")
    private Long id;

    private String uuid;

    private LocalDateTime expiredDateTime;

    public AdminSession() {
        this(null, UUID.randomUUID().toString(), LocalDateTime.now().plusSeconds(SESSION_SECONDS));
    }

    public AdminSession(Long id, String uuid, LocalDateTime expiredDateTime) {
        this.id = id;
        this.uuid = uuid;
        this.expiredDateTime = expiredDateTime;
    }

    public void validateExpired() {
        if (expiredDateTime.isBefore(LocalDateTime.now())) {
            throw new AdminException(ADMIN_AUTH_FAIL);
        }
    }
}

package dev.tripdraw.admin.application;

import static dev.tripdraw.admin.exception.AdminExceptionType.ADMIN_AUTH_FAIL;

import dev.tripdraw.admin.domain.Admin;
import dev.tripdraw.admin.domain.AdminRepository;
import dev.tripdraw.admin.domain.AdminSession;
import dev.tripdraw.admin.domain.AdminSessionRepository;
import dev.tripdraw.admin.dto.AdminLoginRequest;
import dev.tripdraw.admin.exception.AdminException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class AdminAuthService {

    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;
    private final AdminSessionRepository adminSessionRepository;

    public String login(AdminLoginRequest adminLoginRequest) {
        Admin admin = adminRepository.findAdminByEmail(adminLoginRequest.email())
                .orElseThrow(() -> new AdminException(ADMIN_AUTH_FAIL));
        admin.validateFailCount();

        if (!passwordEncoder.checkPassword(adminLoginRequest.password(), admin.password())) {
            admin.increaseFailCount();
            throw new AdminException(ADMIN_AUTH_FAIL);
        }

        admin.resetFailCount();
        AdminSession adminSession = adminSessionRepository.save(new AdminSession());
        return adminSession.uuid();
    }

    public void validateSession(String uuid) {
        AdminSession adminSession = adminSessionRepository.findAdminSessionByUuid(uuid)
                .orElseThrow(() -> new AdminException(ADMIN_AUTH_FAIL));
        adminSession.validateExpired();
    }
}

package dev.tripdraw.admin.application;

import static dev.tripdraw.admin.exception.AdminExceptionType.ADMIN_AUTH_FAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.tripdraw.admin.domain.Admin;
import dev.tripdraw.admin.domain.AdminRepository;
import dev.tripdraw.admin.domain.AdminSessionRepository;
import dev.tripdraw.admin.dto.AdminLoginRequest;
import dev.tripdraw.admin.exception.AdminException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Transactional
@SpringBootTest
class AdminAuthServiceTest {

    @Autowired
    private AdminAuthService adminAuthService;

    @Autowired
    private AdminSessionRepository adminSessionRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void 입력한_이메일에_해당하는_관리자가_없다면_예외가_발생한다() {
        // given
        AdminLoginRequest adminLoginRequest = new AdminLoginRequest("email@naver.com", "password");

        // expect
        assertThatThrownBy(() -> adminAuthService.login(adminLoginRequest))
                .isInstanceOf(AdminException.class)
                .hasMessage(ADMIN_AUTH_FAIL.message());
    }

    @Test
    void 비밀번호를_5회_이상_틀리는_경우_예외가_발생한다() {
        // given
        Admin admin = new Admin(1L, "email@naver.com", passwordEncoder.encode("password"), 5L);
        adminRepository.save(admin);
        AdminLoginRequest adminLoginRequest = new AdminLoginRequest(admin.email(), "password");

        // expect
        assertThatThrownBy(() -> adminAuthService.login(adminLoginRequest))
                .isInstanceOf(AdminException.class)
                .hasMessage(ADMIN_AUTH_FAIL.message());
    }

    @Test
    void 로그인에_실패한다면_예외가_발생한다() {
        // given
        Admin admin = adminRepository.save(new Admin("email@naver.com", passwordEncoder.encode("password")));
        AdminLoginRequest adminLoginRequest = new AdminLoginRequest(admin.email(), "invalidPassword");

        // expect
        assertThatThrownBy(() -> adminAuthService.login(adminLoginRequest))
                .isInstanceOf(AdminException.class)
                .hasMessage(ADMIN_AUTH_FAIL.message());
    }

    @Test
    void 로그인에_성공하는_경우_계정의_비밀번호_실패_횟수가_초기화_된다() {
        // given
        Admin admin = new Admin(1L, "email@naver.com", passwordEncoder.encode("password"), 1L);
        adminRepository.save(admin);
        AdminLoginRequest adminLoginRequest = new AdminLoginRequest(admin.email(), "password");

        // when
        adminAuthService.login(adminLoginRequest);

        // then
        Admin findAdmin = adminRepository.findAdminByEmail(admin.email())
                .orElseThrow();
        assertThat(findAdmin.failCount()).isZero();
    }

    @Test
    void 로그인에_성공하는_경우_세션을_저장하고_세션의_UUID를_반환한다() {
        // given
        Admin admin = new Admin(1L, "email@naver.com", passwordEncoder.encode("password"), 1L);
        adminRepository.save(admin);
        AdminLoginRequest adminLoginRequest = new AdminLoginRequest(admin.email(), "password");

        // when
        String uuid = adminAuthService.login(adminLoginRequest);

        // then
        assertThat(adminSessionRepository.findAdminSessionByUuid(uuid)).isPresent();
    }
}
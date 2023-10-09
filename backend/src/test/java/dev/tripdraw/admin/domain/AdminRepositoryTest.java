package dev.tripdraw.admin.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import dev.tripdraw.common.config.JpaConfig;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Import(JpaConfig.class)
@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest
class AdminRepositoryTest {

    @Autowired
    private AdminRepository adminRepository;

    @Test
    void 관리자의_이메일을_이용하여_관리자를_조회한다() {
        // given
        Admin admin = adminRepository.save(new Admin("hello@naver.com", "password"));

        // when
        Optional<Admin> findAdmin = adminRepository.findAdminByEmail(admin.email());

        // then
        assertThat(findAdmin).isPresent();
    }
}

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
class AdminSessionRepositoryTest {

    @Autowired
    private AdminSessionRepository adminSessionRepository;

    @Test
    void UUID를_이용하여_adminSession을_조회한다() {
        // given
        AdminSession adminSession = adminSessionRepository.save(new AdminSession());

        // when
        Optional<AdminSession> findAdminSession = adminSessionRepository.findAdminSessionByUuid(adminSession.uuid());

        // then
        assertThat(findAdminSession).isPresent();
    }
}

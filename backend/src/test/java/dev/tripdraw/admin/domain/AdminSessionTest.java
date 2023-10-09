package dev.tripdraw.admin.domain;

import static dev.tripdraw.admin.exception.AdminExceptionType.AUTH_FAIL;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.tripdraw.admin.exception.AdminException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AdminSessionTest {

    @Test
    void 만료_기한이_지난_경우_예외가_발생한다() {
        // given
        AdminSession adminSession = new AdminSession(1L, "UUID", LocalDateTime.now().minusMinutes(1));

        // expect
        assertThatThrownBy(adminSession::validateExpired)
                .isInstanceOf(AdminException.class)
                .hasMessage(AUTH_FAIL.message());
    }
}

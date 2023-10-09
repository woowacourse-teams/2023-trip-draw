package dev.tripdraw.admin.domain;

import static dev.tripdraw.admin.exception.AdminExceptionType.AUTH_FAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.tripdraw.admin.exception.AdminException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AdminTest {

    @Test
    void 인증에_실패하면_인증_실패_예외를_던진다() {
        // given
        Admin admin = new Admin("hello", "world");

        // expect
        assertThatThrownBy(() -> admin.validatePassword("fail"))
                .isInstanceOf(AdminException.class)
                .hasMessage(AUTH_FAIL.message());
    }

    @Test
    void 인증에_실패하면_인증_실패_카운트가_증가한다() {
        // given
        Admin admin = new Admin("hello", "world");

        // when
        assertThatThrownBy(() -> admin.validatePassword("fail"))
                .isInstanceOf(AdminException.class)
                .hasMessage(AUTH_FAIL.message());

        // then
        assertThat(admin.failCount()).isEqualTo(1L);
    }

    @Test
    void 인증에_성공하면_예외가_발생하지_않는다() {
        // given
        Admin admin = new Admin("hello", "world");

        // expect
        assertThatNoException().isThrownBy(() -> admin.validatePassword("world"));
    }
}

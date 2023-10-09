package dev.tripdraw.admin.domain;

import static dev.tripdraw.admin.exception.AdminExceptionType.AUTH_FAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.tripdraw.admin.exception.AdminException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AdminTest {

    @Test
    void 인증_실패_카운트가_5회를_초과하는_경우_예외를_던진다() {
        // given
        Admin admin = new Admin("hello", "world");
        for (int i = 0; i < 5; i++) {
            admin.increaseFailCount();
        }

        // expect
        assertThatThrownBy(admin::validateFailCount)
                .isInstanceOf(AdminException.class)
                .hasMessage(AUTH_FAIL.message());
    }

    @Test
    void 인증_실패_카운트를_증가시킨다() {
        // given
        Admin admin = new Admin("hello", "world");

        // when
        admin.increaseFailCount();

        // then
        assertThat(admin.failCount()).isOne();
    }

    @Test
    void 인증_실패_카운트를_초기화시킨다() {
        // given
        Admin admin = new Admin("hello", "world");
        admin.increaseFailCount();

        // when
        admin.resetFailCount();

        // expect
        assertThat(admin.failCount()).isZero();
    }
}

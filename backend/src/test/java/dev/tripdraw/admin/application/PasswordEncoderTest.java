package dev.tripdraw.admin.application;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PasswordEncoderTest {

    private PasswordEncoder passwordEncoder = new PasswordEncoder();

    @Test
    void Bcrypt로_암호화한다() {
        // given
        String password = "hello";

        // when
        String encodedPassword = passwordEncoder.encode(password);

        // then
        assertThat(passwordEncoder.checkPassword(password, encodedPassword)).isTrue();
    }

    @Test
    void 동일한_비밀번호인지_확인한다() {
        // given
        String password = "hello";
        String encodedPassword = passwordEncoder.encode(password);

        // expect
        assertThat(passwordEncoder.checkPassword(password, encodedPassword)).isTrue();
    }
}

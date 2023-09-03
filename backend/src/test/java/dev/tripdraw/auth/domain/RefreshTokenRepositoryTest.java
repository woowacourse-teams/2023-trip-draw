package dev.tripdraw.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DataJpaTest
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    void 토큰을_입력받아_refreshToken_객체를_반환한다() {
        // given
        RefreshToken refreshToken = new RefreshToken(1L, "refreshToken");
        refreshTokenRepository.save(refreshToken);

        // when
        Optional<RefreshToken> findToken = refreshTokenRepository.findByToken(refreshToken.token());

        // then
        assertThat(findToken).isPresent();
    }

    @Test
    void 사용자_아이디를_입력받아_해당되는_모든_RefreshToken을_제거한다() {
        // given
        refreshTokenRepository.save(new RefreshToken(1L, "refreshToken"));
        refreshTokenRepository.save(new RefreshToken(1L, "refreshToken"));

        // when
        refreshTokenRepository.deleteByMemberId(1L);

        // then
        assertThat(refreshTokenRepository.count()).isZero();
    }
}

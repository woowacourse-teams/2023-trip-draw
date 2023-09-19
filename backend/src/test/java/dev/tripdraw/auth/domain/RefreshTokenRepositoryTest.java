package dev.tripdraw.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dev.tripdraw.common.config.JpaConfig;
import dev.tripdraw.common.config.QueryDslConfig;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DataJpaTest
@Import({JpaConfig.class, QueryDslConfig.class})
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    void 토큰을_입력받아_refreshToken_객체를_반환한다() {
        // given
        long memberId = 1L;
        RefreshToken refreshToken = new RefreshToken(memberId, "refreshToken");
        refreshTokenRepository.save(refreshToken);

        // when
        Optional<RefreshToken> findToken = refreshTokenRepository.findByToken(refreshToken.token());

        // then
        assertThat(findToken).isPresent();
    }

    @Test
    void 사용자_아이디를_입력받아_해당되는_모든_RefreshToken을_제거한다() {
        // given
        long memberId = 1L;
        refreshTokenRepository.save(new RefreshToken(memberId, "refreshToken"));
        refreshTokenRepository.save(new RefreshToken(memberId, "refreshToken"));

        // when
        refreshTokenRepository.deleteByMemberId(memberId);

        // then
        assertThat(refreshTokenRepository.count()).isZero();
    }
}

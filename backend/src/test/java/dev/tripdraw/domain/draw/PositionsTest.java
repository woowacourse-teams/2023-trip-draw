package dev.tripdraw.domain.draw;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PositionsTest {

    @Test
    void 이미지_사이즈에_맞춰_중앙_정렬_한_값을_반환한다() {
        // given
        Positions positions = new Positions(List.of(
                new Position(0, 600), new Position(48, 550), new Position(135, 562), new Position(228, 536),
                new Position(267, 424), new Position(292, 364), new Position(148, 274), new Position(63, 0)
        ));

        // when
        Positions alignedPositions = positions.alignPositions(800, 600);

        // then
        assertThat(alignedPositions.items()).containsExactly(
                new Position(254, 100), new Position(302, 150), new Position(389, 138), new Position(482, 164),
                new Position(521, 276), new Position(546, 336), new Position(402, 426), new Position(317, 700)
        );
    }
}

package dev.tripdraw.domain.draw;

import static dev.tripdraw.exception.draw.DrawExceptionType.INVALID_COORDINATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.tripdraw.exception.draw.DrawException;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CoordinatesTest {

    @Test
    void 입력받는_두_값들의_갯수가_동일하지_않은_경우_예외를_던진다() {
        // given
        List<Double> xValues = List.of(1.0, 2.0, 3.0);
        List<Double> yValues = List.of(1.0, 2.0);

        // expect
        assertThatThrownBy(() -> Coordinates.of(xValues, yValues))
                .isInstanceOf(DrawException.class)
                .hasMessage(INVALID_COORDINATE.getMessage());
    }

    @Test
    void 입력받는_두_값들의_갯수가_동일한_경우_예외를_던지지_않는다() {
        // given
        List<Double> xValues = List.of(1.0, 2.0, 3.0);
        List<Double> yValues = List.of(1.0, 2.0, 3.0);

        // expect
        assertThatNoException().isThrownBy(() -> Coordinates.of(xValues, yValues));
    }

    @Test
    void 이미지_사이즈를_입력받아_이미지_생성에_사용할_점들의_위치를_반환받는다() {
        // given
        List<Double> xValues = List.of(
                126.96352960597338, 126.96987292787792, 126.98128481452298, 126.99360339342958,
                126.99867565340067, 127.001935378366117, 126.9831048919687, 126.97189273528845
        );
        List<Double> yValues = List.of(
                37.590841000217125, 37.58435564234159, 37.58594375113966, 37.58248524741927,
                37.56778118088622, 37.55985240444085, 37.548030119488665, 37.5119879225856
        );
        Coordinates coordinates = Coordinates.of(xValues, yValues);

        // when
        Positions positions = coordinates.calculatePositions(600);

        // then
        assertThat(positions.items()).containsExactly(
                new Position(0, 600), new Position(48, 550), new Position(135, 562), new Position(228, 536),
                new Position(267, 424), new Position(292, 364), new Position(148, 274), new Position(63, 0)
        );
    }
}

package dev.tripdraw.common.log;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class QueryCounterTest {

    @Test
    void 쿼리_개수를_하나_증가시킨다() {
        // given
        QueryCounter queryCounter = new QueryCounter();

        // when
        queryCounter.increase();

        // then
        assertThat(queryCounter.count()).isOne();
    }

    @ParameterizedTest
    @CsvSource({"11, true", "10, false"})
    void 쿼리_개수가_경고_개수_이상인지_확인한다(int count, boolean expected) {
        // given
        QueryCounter queryCounter = new QueryCounter();
        for (int i = 0; i < count; i++) {
            queryCounter.increase();
        }

        // expect
        assertThat(queryCounter.isOverWarningCount()).isEqualTo(expected);
    }
}

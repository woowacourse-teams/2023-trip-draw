package dev.tripdraw.common.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PagingTest {

    @ParameterizedTest
    @CsvSource({"19, false", "20, false", "21, true"})
    void 다음_페이지가_있는지_확인한다(int size, boolean expected) {
        // given
        Paging paging = new Paging(1L, 20);

        // when
        boolean actual = paging.hasNextPage(size);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}

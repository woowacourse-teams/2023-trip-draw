package dev.tripdraw.post.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PostPagingTest {

    @ParameterizedTest
    @CsvSource({"19, false", "20, false", "21, true"})
    void 다음_페이지가_있는지_확인한다(int size, boolean expected) {
        // given
        PostPaging postPaging = new PostPaging(1L, 20);

        // when
        boolean actual = postPaging.hasNextPage(size);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void limit이_최대값을_넘기면_최대값으로_조정된다() {
        // given
        int limit = 101;

        // when
        PostPaging postPaging = new PostPaging(1L, limit);

        // then
        assertThat(postPaging.limit()).isEqualTo(100);
    }

    @Test
    void limit이_null이면_기본값으로_조정된다() {
        // given
        Integer limit = null;

        // when
        PostPaging postPaging = new PostPaging(1L, limit);

        // then
        assertThat(postPaging.limit()).isEqualTo(20);
    }
}

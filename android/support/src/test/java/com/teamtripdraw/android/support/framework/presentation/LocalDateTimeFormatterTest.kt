package com.teamtripdraw.android.support.framework.presentation

import com.teamtripdraw.android.support.framework.presentation.LocalDateTimeFormatter.isoRemoveNanoSecondFormatter
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.time.LocalDateTime

class LocalDateTimeFormatterTest {

    @Test
    fun `isoRemoveNanoSecondFormatter 포매팅의 형식에서 ns 단위의 제거가 일어난다`() {
        // given
        val currentDateTime = LocalDateTime.now()

        // when
        val formattedDateTime = currentDateTime.format(isoRemoveNanoSecondFormatter)

        // then
        assertThat(formattedDateTime.matches(Regex("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}"))).isTrue
    }
}

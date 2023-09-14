package com.teamtripdraw.android.domain.model.post

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class PostWritingValidStateTest {

    @ParameterizedTest
    @ValueSource(strings = ["", "   "])
    fun `글의 제목이 공백으로만 이루어지거나 글자가 없다면 빈 제목 상태이다`(title: String) {
        // given
        val writing: String = "otter"

        // when
        val actual = PostWritingValidState.getValidState(title, writing)
        val expected = PostWritingValidState.EMPTY_TITLE

        // then
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "   "])
    fun `글의 내용이 공백으로만 이루어지거나 글자가 없다면 빈 글 상태이다`(writing: String) {
        // given
        val title: String = "otter"

        // when
        val actual = PostWritingValidState.getValidState(title, writing)
        val expected = PostWritingValidState.EMPTY_WRITING

        // then
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `글의 제목과 내용이 채워져있다면 사용 가능 상태이다`() {
        // given
        val title: String = "otter"
        val writing: String = "otter"

        // when
        val actual = PostWritingValidState.getValidState(title, writing)
        val expected = PostWritingValidState.AVAILABLE

        // then
        assertThat(actual).isEqualTo(expected)
    }
}

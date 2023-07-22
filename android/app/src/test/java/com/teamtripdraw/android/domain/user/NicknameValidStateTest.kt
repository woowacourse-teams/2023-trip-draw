package com.teamtripdraw.android.domain.user

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class NicknameValidStateTest {

    @Test
    fun `이름의 길이가 0이라면 비어있는 상태이다`() {
        // given
        val nickname: String = ""

        // when
        val actual: NicknameValidState = NicknameValidState.getValidState(nickname)
        val expected: NicknameValidState = NicknameValidState.EMPTY

        // then
        assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @ValueSource(strings = ["핑", "달멧", "핑구수달멧돼지브레멘"])
    fun `이름의 길이가 1이상 10이하인 경우 사용 가능한 상태이다`(nickname: String) {
        // when
        val actual: NicknameValidState = NicknameValidState.getValidState(nickname)
        val expected: NicknameValidState = NicknameValidState.AVAILABLE

        // then
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `이름의 길이가 10 초과인 경우 제한 초과 상태이다`() {
        // given
        val nickname: String = "핑구수달멧돼지는브레멘"

        // when
        val actual: NicknameValidState = NicknameValidState.getValidState(nickname)
        val expected: NicknameValidState = NicknameValidState.EXCEED_LIMIT

        // then
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `이름에 공백이 있을 경우 공백 포함 상태이다`() {
        // given
        val nickname: String = "핑구수달멧돼지 "

        // when
        val actual: NicknameValidState = NicknameValidState.getValidState(nickname)
        val expected: NicknameValidState = NicknameValidState.CONTAIN_BLANK

        // then
        assertThat(actual).isEqualTo(expected)
    }
}

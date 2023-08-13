package com.teamtripdraw.android.domain.model.trip

import com.teamtripdraw.android.domain.model.user.NicknameValidState
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class TripTitleValidStateTest {

    @ParameterizedTest
    @ValueSource(strings = ["", "  "])
    fun `제목이 비어있거나 빈 칸을 포함하고 있다면 빈 칸 포함 상태이다`(title: String) {
        // when
        val actual = TripTitleValidState.getValidState(title)
        val expected = TripTitleValidState.CONTAIN_BLANK

        // then
        Assertions.assertThat(actual).isEqualTo(expected)
    }

    @ParameterizedTest
    @ValueSource(strings = ["핑구핑구", "핑구의 여행", "핑         구"])
    fun `제목의 길이가 1이상 100이하인 경우 사용 가능한 상태이다`(title: String) {
        // when
        val actual = TripTitleValidState.getValidState(title)
        val expected = TripTitleValidState.AVAILABLE

        // then
        Assertions.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `이름의 길이가 100 초과인 경우 제한 초과 상태이다`() {
        // given
        val title = "일이삼사오육칠팔구십" +
            "일이삼사오육칠팔구십" +
            "일이삼사오육칠팔구십" +
            "일이삼사오육칠팔구십" +
            "일이삼사오육칠팔구십" +
            "일이삼사오육칠팔구십" +
            "일이삼사오육칠팔구십" +
            "일이삼사오육칠팔구십" +
            "일이삼사오육칠팔구십" +
            "일이삼사오육칠팔구십" +
            "일"

        // when
        val actual = TripTitleValidState.getValidState(title)
        val expected = TripTitleValidState.EXCEED_LIMIT

        // then
        Assertions.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `이름에 공백이 있을 경우 공백 포함 상태이다`() {
        // given
        val nickname = "핑구수달멧돼지 "

        // when
        val actual: NicknameValidState = NicknameValidState.getValidState(nickname)
        val expected: NicknameValidState = NicknameValidState.CONTAIN_BLANK

        // then
        Assertions.assertThat(actual).isEqualTo(expected)
    }
}

package com.teamtripdraw.android.ui.signUp

import com.teamtripdraw.android.rule.InstantTaskExecutorRule
import com.teamtripdraw.android.rule.MainDispatcherRule
import com.teamtripdraw.android.domain.exception.nicknameSetup.DuplicateNickNameException
import com.teamtripdraw.android.domain.repository.NicknameSetupRepository
import com.teamtripdraw.android.domain.user.NicknameValidState
import io.mockk.coEvery
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class NicknameSetupViewModelTest {

    // system under test
    private lateinit var sut: NicknameSetupViewModel
    private lateinit var repository: NicknameSetupRepository

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    internal fun setUp() {
        repository = mockk()
        sut = NicknameSetupViewModel(repository)
    }

    @Test
    fun `닉네임이 변경될 경우 닉네임 유효성 상태를 변경한다`() {
        // given
        val before = sut.nicknameState.value

        // when
        sut.nickname.value = "pingter"
        sut.nicknameChangedEvent()
        val after = sut.nicknameState.value

        // then
        assertThat(before).isNotEqualTo(after)
    }

    @Test
    fun `닉네임 설정이 완료될 경우 닉네임 설정 완료 상태를 true로 변경한다`() {
        // given
        sut.nickname.value = "pingter"
        val result: Result<Unit> = Result.success(Unit)
        coEvery { repository.setNickName(sut.nickname.value!!) } returns result

        // when
        sut.onNicknameSetupComplete()

        // then
        assertThat(sut.nicknameSetupCompleteEvent.value?.content).isTrue
    }

    @Test
    fun `닉네임이 중복될 경우 닉네임 유효성 상태를 중복 상태로 변경한다`() {
        // given
        sut.nickname.value = "pingter"
        val result: Result<Unit> = Result.failure(DuplicateNickNameException("중복된 닉네임 입니다."))
        coEvery { repository.setNickName(sut.nickname.value!!) } returns result

        // when
        sut.onNicknameSetupComplete()

        // then
        assertThat(sut.nicknameState.value).isEqualTo(NicknameValidState.DUPLICATE)
    }
}

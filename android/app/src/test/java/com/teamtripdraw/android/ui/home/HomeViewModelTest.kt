package com.teamtripdraw.android.ui.home

import com.teamtripdraw.android.DefaultViewModelTest
import com.teamtripdraw.android.domain.constants.NULL_SUBSTITUTE_TRIP_ID
import com.teamtripdraw.android.domain.repository.PointRepository
import com.teamtripdraw.android.domain.repository.TripRepository
import com.teamtripdraw.android.ui.home.HomeUiState.BEFORE_TRIP
import com.teamtripdraw.android.ui.home.HomeUiState.ON_TRIP
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Test

class HomeViewModelTest : DefaultViewModelTest() {

    // class under test
    private lateinit var cut: HomeViewModel
    private lateinit var tripRepository: TripRepository
    private lateinit var pointRepository: PointRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        tripRepository = mockk(relaxed = true)
        pointRepository = mockk()
        cut = HomeViewModel(tripRepository, pointRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `현재 사용자가 여행 시작 전 이라면 뷰모델 초기화시 최초 UiState가 여행 전 상태를 나타낸다`() {
        // given
        coEvery { tripRepository.getCurrentTripId() } returns NULL_SUBSTITUTE_TRIP_ID

        // when
        cut = HomeViewModel(tripRepository, pointRepository)

        // then
        assertEquals(cut.homeUiState.value, BEFORE_TRIP)
    }

    @Test
    fun `현재 사용자가 여행 중 이라면 뷰모델 초기화시 최초 UiState가 여행중 상태를 나타낸다`() {
        // given
        val testTripId = 1L
        coEvery { tripRepository.getCurrentTripId() } returns testTripId

        // when
        cut = HomeViewModel(tripRepository, pointRepository)

        // then
        assertEquals(cut.homeUiState.value, ON_TRIP)
    }

    @Test
    fun `현재 사용자가 여행 시작 전 일때 여행 시작시 UiState가 여행중 상태로 변경된다`() {
        // given
        // 사용자 여행 시작전 상태 초기화 코드
        coEvery { tripRepository.getCurrentTripId() } returns NULL_SUBSTITUTE_TRIP_ID
        coEvery { tripRepository.startTrip() } returns Result.success(Unit)

        // when
        cut.startTrip()

        // then
        assertEquals(cut.homeUiState.value, ON_TRIP)
    }
}

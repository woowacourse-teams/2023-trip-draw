package com.teamtripdraw.android.ui.post.writing

import com.teamtripdraw.android.DefaultViewModelTest
import com.teamtripdraw.android.domain.model.point.Point
import com.teamtripdraw.android.domain.repository.PointRepository
import com.teamtripdraw.android.domain.repository.PostRepository
import com.teamtripdraw.android.domain.repository.TripRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Test
import org.junit.jupiter.api.Assertions
import java.time.LocalDateTime

internal class PostWritingViewModelTest : DefaultViewModelTest() {
    // system under test
    private lateinit var sut: PostWritingViewModel
    private lateinit var postRepository: PostRepository
    private lateinit var pointRepository: PointRepository
    private lateinit var tripRepository: TripRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        postRepository = mockk()
        pointRepository = mockk()
        tripRepository = mockk(relaxed = true)

        sut = PostWritingViewModel(
            postRepository = postRepository,
            pointRepository = pointRepository,
            tripRepository = tripRepository
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `여행 정보 중 Point를 가져온다`() {
        // given
        val pointId: Long = 0
        val expectedLat = 0.1
        val expectedLng = 0.2
        val point =
            Point(pointId, expectedLat, expectedLng, false, LocalDateTime.of(2023, 8, 2, 10, 10))
        val result: Result<Point> = Result.success(point)
        coEvery { pointRepository.getPoint(any(), any()) } returns result

        // when
        sut.initTripData(pointId)
        val actualLat = sut.point.value?.latitude
        val actualLng = sut.point.value?.longitude

        // then
        Assertions.assertEquals(actualLat, expectedLat)
        Assertions.assertEquals(actualLng, expectedLng)
    }
}

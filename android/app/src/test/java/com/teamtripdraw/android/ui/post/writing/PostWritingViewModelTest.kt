package com.teamtripdraw.android.ui.post.writing

import com.teamtripdraw.android.DefaultViewModelTest
import com.teamtripdraw.android.domain.model.point.Point
import com.teamtripdraw.android.domain.model.post.Post
import com.teamtripdraw.android.domain.repository.PointRepository
import com.teamtripdraw.android.domain.repository.PostRepository
import com.teamtripdraw.android.domain.repository.TripRepository
import com.teamtripdraw.android.testDouble.DummyPrePoint
import com.teamtripdraw.android.testDouble.DummyPrePost
import com.teamtripdraw.android.testDouble.FakePointRepository
import com.teamtripdraw.android.testDouble.FakePostRepository
import com.teamtripdraw.android.testDouble.FakeTripRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

internal class PostWritingViewModelTest : DefaultViewModelTest() {

    // system under test
    private lateinit var sut: PostWritingViewModel
    private lateinit var postRepository: PostRepository
    private lateinit var pointRepository: PointRepository
    private lateinit var tripRepository: TripRepository

    @Before
    fun setUp() {
        postRepository = FakePostRepository()
        pointRepository = FakePointRepository()
        tripRepository = FakeTripRepository()

        sut = PostWritingViewModel(
            postRepository = postRepository,
            pointRepository = pointRepository,
            tripRepository = tripRepository,
        )
    }

    @Test
    fun `감상을 새로 작성할 때는 현재 여행 정보와 감상 위치 정보를 불러와 적용한다`() = runTest {
        // given
        tripRepository.startTrip()
        val tripId = tripRepository.getCurrentTripId()

        var point: Point? = null
        pointRepository.createRecordingPoint(prePoint = DummyPrePoint(), tripId = tripId)
        pointRepository.getPoint(0, tripId).onSuccess { point = it }

        // when
        sut.initWritingMode(WritingMode.NEW, point!!.pointId)

        // then
        assertEquals(sut.point.value!!, point)
    }

    @Test
    fun `감상을 수정할 때는 기존 감상 정보를 불러와 적용한다`() = runTest {
        // given
        var post: Post? = null
        postRepository.addPost(DummyPrePost())
        postRepository.getPost(0).onSuccess { post = it }

        // when
        sut.initWritingMode(WritingMode.EDIT, post!!.postId)

        // then
        assertEquals(post!!.address, sut.address.value)
        assertEquals(post!!.title, sut.title.value)
        assertEquals(post!!.writing, sut.writing.value)
        assertEquals(post!!.postImageUrl, sut.imageUri.value)
    }
}

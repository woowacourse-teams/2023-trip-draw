package com.teamtripdraw.android.ui.post.viewer

import com.teamtripdraw.android.DefaultViewModelTest
import com.teamtripdraw.android.domain.model.point.Point
import com.teamtripdraw.android.domain.model.post.Post
import com.teamtripdraw.android.domain.repository.PostRepository
import com.teamtripdraw.android.domain.repository.TripRepository
import com.teamtripdraw.android.ui.model.mapper.toPresentation
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import java.time.LocalDateTime

class PostViewerViewModelTest : DefaultViewModelTest() {

    // system under test
    private lateinit var sut: PostViewerViewModel
    private lateinit var tripRepository: TripRepository
    private lateinit var postRepository: PostRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        tripRepository = mockk(relaxed = true)
        postRepository = mockk()
        sut = PostViewerViewModel(tripRepository, postRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `감상 목록을 가져오는 것을 성공했을 경우 감상 목록의 값을 변경한다`() {
        // given
        val posts = listOf(
            Post(
                postId = 0,
                tripId = 0,
                title = "title",
                writing = "writing",
                address = "address",
                point = Point(
                    pointId = 0,
                    latitude = 0.0,
                    longitude = 0.0,
                    recordedAt = LocalDateTime.of(2023, 8, 2, 3, 27),
                    hasPost = false,
                ),
                postImageUrl = null,
                routeImageUrl = null,
            ),
        )
        val result: Result<List<Post>> = Result.success(posts)
        coEvery { postRepository.getTripPosts(any()) } returns result

        // when
        sut.getPosts()
        val expected = posts.map { it.toPresentation() }

        // then
        assertEquals(expected, sut.posts.value)
    }

    @Test
    fun `감상 목록을 가져오는 것을 실패했을 경우 에러 유발 상태의 값을 변경한다`() {
        // given
        val result: Result<List<Post>> = Result.failure(IllegalStateException())
        coEvery { postRepository.getTripPosts(any()) } returns result

        // when
        sut.getPosts()

        // then
        assertTrue(requireNotNull(sut.postErrorEvent.value?.content))
    }

    @Test
    fun `특정 감상이 선택되었을 경우 감상 선택 상태를 변경한다`() {
        // given
        val before = sut.openPostDetailEvent.value

        // when
        sut.openPostDetail(0)
        val after = sut.openPostDetailEvent.value

        // then
        assertNotEquals(after, before)
    }
}

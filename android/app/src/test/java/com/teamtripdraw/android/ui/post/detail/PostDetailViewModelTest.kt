package com.teamtripdraw.android.ui.post.detail

import com.teamtripdraw.android.DefaultViewModelTest
import com.teamtripdraw.android.domain.model.point.Point
import com.teamtripdraw.android.domain.model.post.Post
import com.teamtripdraw.android.domain.repository.PostRepository
import com.teamtripdraw.android.ui.model.mapper.toDetailPresentation
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import java.time.LocalDateTime

class PostDetailViewModelTest : DefaultViewModelTest() {

    // system under test
    private lateinit var sut: PostDetailViewModel
    private lateinit var postRepository: PostRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        postRepository = mockk()
        sut = PostDetailViewModel(postRepository)
        sut.postId.value = 0L
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `감상 기록을 가져는 것을 성공했을 경우 감상 기록의 값을 변경한다`() {
        // given
        val post = Post(
            postId = 0, tripId = 0, title = "", writing = "", address = "", point = Point(
                pointId = 0,
                latitude = 0.0,
                longitude = 0.0,
                hasPost = false,
                recordedAt = LocalDateTime.of(2023, 8, 2, 7, 40)
            ), postImageUrl = null, routeImageUrl = null
        )
        val result: Result<Post> = Result.success(post)
        coEvery { postRepository.getPost(any()) } returns result

        // when
        sut.getPost()

        // then
        assertEquals(post.toDetailPresentation(), sut.postDetail.value)
    }

    @Test
    fun `감상 삭제를 성공했을 경우 감상 삭제 상태 값을 변경한다`() {
        // given
        coEvery { postRepository.deletePost(any()) } returns Result.success(Unit)

        // when
        sut.deletePost()

        // then
        assertTrue(requireNotNull(sut.postDeletedEvent.value?.content))
    }
}

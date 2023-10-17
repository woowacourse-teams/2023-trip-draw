package com.teamtripdraw.android.ui.post.detail

import com.teamtripdraw.android.DefaultViewModelTest
import com.teamtripdraw.android.domain.model.post.Post
import com.teamtripdraw.android.domain.repository.PostRepository
import com.teamtripdraw.android.getPost
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

class PostDetailViewModelTest : DefaultViewModelTest() {

    // system under test
    private lateinit var sut: PostDetailViewModel
    private lateinit var postRepository: PostRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        postRepository = mockk()
        sut = PostDetailViewModel(postRepository)
        sut.initPostId(0L)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `감상 기록을 가져는 것을 성공했을 경우 감상 기록의 값을 변경한다`() {
        // given
        val post = getPost()
        val result: Result<Post> = Result.success(post)
        coEvery { postRepository.getPostByPostId(any()) } returns result

        // when
        sut.fetchPost()

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
        assertTrue(requireNotNull(sut.postDeleteCompletedEvent.value))
    }
}

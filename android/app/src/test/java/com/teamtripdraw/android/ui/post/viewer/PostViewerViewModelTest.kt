package com.teamtripdraw.android.ui.post.viewer

import com.teamtripdraw.android.DefaultViewModelTest
import com.teamtripdraw.android.domain.model.post.Post
import com.teamtripdraw.android.domain.repository.PostRepository
import com.teamtripdraw.android.testDouble.DummyPost
import com.teamtripdraw.android.ui.model.mapper.toPresentation
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue

class PostViewerViewModelTest : DefaultViewModelTest() {

    // system under test
    private lateinit var sut: PostViewerViewModel
    private lateinit var postRepository: PostRepository

    @Before
    fun setUp() {
        postRepository = mockk()
        sut = PostViewerViewModel(postRepository)
    }

//    @Test
//    fun `감상 목록을 가져오는 것을 성공했을 경우 감상 목록의 값을 변경한다`() {
//        // given
//        val posts = listOf(DummyPost())
//        val result: Result<List<Post>> = Result.success(posts)
//        coEvery { postRepository.getAllPosts(any()) } returns result
//
//        // when
//        sut.fetchPosts()
//        val expected = posts.map { it.toPresentation() }
//
//        // then
//        assertEquals(expected, sut.posts.value)
//    }

    @Test
    fun `감상 목록을 가져오는 것을 실패했을 경우 에러 유발 상태의 값을 변경한다`() {
        // given
        val result: Result<List<Post>> = Result.failure(IllegalStateException())
        coEvery { postRepository.getAllPosts(any()) } returns result

        // when
        sut.fetchPosts()

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

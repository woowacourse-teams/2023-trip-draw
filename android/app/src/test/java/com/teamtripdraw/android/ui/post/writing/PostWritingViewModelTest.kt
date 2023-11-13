package com.teamtripdraw.android.ui.post.writing

import com.teamtripdraw.android.DefaultViewModelTest
import com.teamtripdraw.android.domain.repository.GeocodingRepository
import com.teamtripdraw.android.domain.repository.PointRepository
import com.teamtripdraw.android.domain.repository.PostRepository
import com.teamtripdraw.android.domain.repository.TripRepository
import io.mockk.mockk
import org.junit.Before

internal class PostWritingViewModelTest : DefaultViewModelTest() {
    // system under test
    private lateinit var sut: PostWritingViewModel
    private lateinit var postRepository: PostRepository
    private lateinit var pointRepository: PointRepository
    private lateinit var tripRepository: TripRepository
    private lateinit var geocodingRepository: GeocodingRepository

    @Before
    fun setUp() {
        postRepository = mockk()
        pointRepository = mockk()
        tripRepository = mockk()
        geocodingRepository = mockk()

        sut = PostWritingViewModel(
            postRepository = postRepository,
            pointRepository = pointRepository,
            geocodingRepository = geocodingRepository,
        )
    }

//    @Test
//    fun `글을 새로 작성할 때는 현재 여행 정보와 감상 기록 지점 정보를 불러와 적용한다`() {
//        // given
//        val pointId: Long = 0
//        val expectedLat = 0.1
//        val expectedLng = 0.2
//        val point = DummyPoint(pointId = pointId, latitude = expectedLat, longitude = expectedLng)
//        val getCurrentTripIdResult: Long = 0
//        coEvery { tripRepository.getCurrentTripId() } returns getCurrentTripIdResult
//        val getPointResult: Result<Point> = Result.success(point)
//        coEvery { pointRepository.getPoint(any(), any()) } returns getPointResult
//
//        // when
//        sut.initPostData(pointId)
//        val actualLat = sut.point.value?.latitude
//        val actualLng = sut.point.value?.longitude
//
//        // then
//        Assertions.assertEquals(actualLat, expectedLat)
//        Assertions.assertEquals(actualLng, expectedLng)
//    }

//    @Test
//    fun `글을 수정할 때는 기존 글의 정보를 불러와 적용한다`() {
//        // given
//        val postId: Long = 0
//        val expectedAddress = "otter 124-23"
//        val expectedTitle = "otter is pokemon"
//        val expectedWriting =
//            "피카츄 라이츄 파이리 꼬부기 버터풀 야도란 피죤투 또가스, 서로 생긴 모습은 달라도 우리는 모두 친구, 산에서 들에서 때리고 뒹굴고 ~~"
//        val expectedImageUri = "https://assets.pokemon.com/assets/cms2/img/pokedex/full/490.png"
//        val expectedPost: Post = DummyPost(
//            postId = postId,
//            address = expectedAddress,
//            title = expectedTitle,
//            writing = expectedWriting,
//            postImageUrl = expectedImageUri,
//        )
//        val expectedGetPostSuccessResult: Result<Post> = Result.success(expectedPost)
//        coEvery { postRepository.getPostByPostId(postId) } returns expectedGetPostSuccessResult
//
//        // when
//        sut.initPostData(postId)
//        val actualAddress = sut.address.value
//        val actualTitle = sut.title.value
//        val actualWriting = sut.writing.value
//        val actualImageUri = sut.imageUri.value
//
//        // then
//        Assertions.assertEquals(expectedAddress, actualAddress)
//        Assertions.assertEquals(expectedTitle, actualTitle)
//        Assertions.assertEquals(expectedWriting, actualWriting)
//        Assertions.assertEquals(expectedImageUri, actualImageUri)
//    }
}

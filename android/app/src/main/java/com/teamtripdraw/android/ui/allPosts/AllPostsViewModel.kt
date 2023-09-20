package com.teamtripdraw.android.ui.allPosts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.teamtripdraw.android.domain.model.post.PostOfAll
import com.teamtripdraw.android.ui.model.UiAllPosts
import com.teamtripdraw.android.ui.model.mapper.toPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AllPostsViewModel @Inject constructor() : ViewModel() {

    private val _posts: MutableLiveData<List<PostOfAll>> = MutableLiveData()
    val posts: LiveData<UiAllPosts> =
        Transformations.map(_posts) { post -> UiAllPosts(post.map { it.toPresentation() }) }

    fun fetchPosts() {
        // dummy data 입니다
        _posts.value = List(100) {
            PostOfAll(
                0,
                0,
                "자고 싶어요",
                "자고 싶긴 한데 이것만 완성하고 자자",
                "서울특별시 강서구 마곡동",
                "https://cdn.aitimes.kr/news/photo/202303/27617_41603_044.jpg",
                "https://cdn.aitimes.kr/news/photo/202303/27617_41603_044.jpg",
                LocalDateTime.of(2023, 9, 18, 2, 53),
            )
        }
    }
}

package com.teamtripdraw.android.ui.allPosts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtripdraw.android.TripDrawApplication
import com.teamtripdraw.android.domain.model.post.PostOfAll
import com.teamtripdraw.android.domain.repository.PostRepository
import com.teamtripdraw.android.ui.model.UiAllPosts
import com.teamtripdraw.android.ui.model.mapper.toPresentation
import kotlinx.coroutines.launch

class AllPostsViewModel(
    private val postRepository: PostRepository,
) : ViewModel() {

    private val _posts: MutableLiveData<List<PostOfAll>> = MutableLiveData()
    val posts: LiveData<UiAllPosts> =
        Transformations.map(_posts) { post -> UiAllPosts(post.map { it.toPresentation() }) }

    fun fetchPosts() {
        viewModelScope.launch {
            postRepository.getAllPosts()
                .onSuccess {
                    _posts.value = it
                }
                .onFailure {
                    TripDrawApplication.logUtil.general.log(it)
                }
        }
    }
}

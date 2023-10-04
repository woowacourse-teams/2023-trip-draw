package com.teamtripdraw.android.ui.allPosts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtripdraw.android.TripDrawApplication
import com.teamtripdraw.android.domain.model.post.PostOfAll
import com.teamtripdraw.android.domain.repository.PostRepository
import com.teamtripdraw.android.support.framework.presentation.event.Event
import com.teamtripdraw.android.ui.model.UiAllPosts
import com.teamtripdraw.android.ui.model.mapper.toPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllPostsViewModel @Inject constructor(
    private val postRepository: PostRepository,
) : ViewModel() {

    private val _posts: MutableLiveData<List<PostOfAll>> = MutableLiveData()
    val posts: LiveData<UiAllPosts> =
        Transformations.map(_posts) { post -> UiAllPosts(post.map { it.toPresentation() }) }

    private val _openPostDetailEvent = MutableLiveData<Event<Long>>()
    val openPostDetailEvent: LiveData<Event<Long>> = _openPostDetailEvent

    private val _openFilterSelectionEvent = MutableLiveData<Boolean>()
    val openFilterSelectionEvent: LiveData<Boolean> = _openFilterSelectionEvent

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

    fun openPostDetail(id: Long) {
        _openPostDetailEvent.value = Event(id)
    }

    fun openFilterSelection() {
        _openFilterSelectionEvent.value = true
    }
}

package com.teamtripdraw.android.ui.post.viewer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtripdraw.android.domain.constants.NULL_SUBSTITUTE_TRIP_ID
import com.teamtripdraw.android.domain.repository.PostRepository
import com.teamtripdraw.android.support.framework.presentation.event.Event
import com.teamtripdraw.android.ui.model.UiPostItem
import com.teamtripdraw.android.ui.model.mapper.toPresentation
import kotlinx.coroutines.launch

class PostViewerViewModel(
    private val postRepository: PostRepository,
) : ViewModel() {

    var tripId: Long = NULL_SUBSTITUTE_TRIP_ID
        private set

    private val _posts: MutableLiveData<List<UiPostItem>> = MutableLiveData()
    val posts: LiveData<List<UiPostItem>> = _posts

    private val _noPostMessageEvent = MutableLiveData<Boolean>()
    val noPostMessageEvent: LiveData<Boolean> = _noPostMessageEvent

    private val _openPostDetailEvent = MutableLiveData<Event<Long>>()
    val openPostDetailEvent: LiveData<Event<Long>> = _openPostDetailEvent

    private val _postErrorEvent = MutableLiveData<Event<Boolean>>()
    val postErrorEvent: LiveData<Event<Boolean>> = _postErrorEvent

    fun updateTripId(tripId: Long) {
        this.tripId = tripId
    }

    fun getPosts() {
        viewModelScope.launch {
            postRepository.getAllPosts(tripId)
                .onSuccess { posts ->
                    _posts.value = posts.map { post -> post.toPresentation() }
                    _noPostMessageEvent.value = posts.isEmpty()
                }
                .onFailure {
                    _postErrorEvent.value = Event(true)
                }
        }
    }

    fun openPostDetail(id: Long) {
        _openPostDetailEvent.value = Event(id)
    }
}

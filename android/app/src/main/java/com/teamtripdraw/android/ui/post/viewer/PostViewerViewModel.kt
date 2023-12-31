package com.teamtripdraw.android.ui.post.viewer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtripdraw.android.TripDrawApplication
import com.teamtripdraw.android.domain.model.post.Post
import com.teamtripdraw.android.domain.model.trip.Trip
import com.teamtripdraw.android.domain.repository.PostRepository
import com.teamtripdraw.android.support.framework.presentation.event.Event
import com.teamtripdraw.android.ui.model.UiPosts
import com.teamtripdraw.android.ui.model.mapper.toPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewerViewModel @Inject constructor(
    private val postRepository: PostRepository,
) : ViewModel() {

    var tripId: Long = Trip.NULL_SUBSTITUTE_ID
        private set

    private val _posts: MutableLiveData<List<Post>> = MutableLiveData()
    val posts: LiveData<UiPosts> =
        Transformations.map(_posts) { post -> UiPosts(post.map { it.toPresentation() }) }

    private val _openPostDetailEvent = MutableLiveData<Event<Long>>()
    val openPostDetailEvent: LiveData<Event<Long>> = _openPostDetailEvent

    private val _postErrorEvent = MutableLiveData<Event<Boolean>>()
    val postErrorEvent: LiveData<Event<Boolean>> = _postErrorEvent

    fun updateTripId(tripId: Long) {
        this.tripId = tripId
    }

    fun fetchPosts() {
        viewModelScope.launch {
            postRepository.getTripPosts(tripId)
                .onSuccess { posts ->
                    _posts.value = posts
                }
                .onFailure {
                    _postErrorEvent.value = Event(true)
                    TripDrawApplication.logUtil.general.log(it)
                }
        }
    }

    fun openPostDetail(id: Long) {
        _openPostDetailEvent.value = Event(id)
    }
}

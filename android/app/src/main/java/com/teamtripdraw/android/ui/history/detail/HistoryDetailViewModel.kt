package com.teamtripdraw.android.ui.history.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtripdraw.android.domain.constants.NULL_SUBSTITUTE_TRIP_ID
import com.teamtripdraw.android.domain.repository.PostRepository
import com.teamtripdraw.android.domain.repository.TripRepository
import com.teamtripdraw.android.support.framework.presentation.event.Event
import com.teamtripdraw.android.ui.model.UiPostItem
import com.teamtripdraw.android.ui.model.UiPreviewTrip
import com.teamtripdraw.android.ui.model.mapper.toPresentation
import kotlinx.coroutines.launch

class HistoryDetailViewModel(
    private val tripRepository: TripRepository,
    private val postRepository: PostRepository,
) : ViewModel() {

    private val _previewTrip: MutableLiveData<UiPreviewTrip> = MutableLiveData()
    val previewTrip: LiveData<UiPreviewTrip> = _previewTrip

    private val tripId get() = previewTrip.value?.id ?: NULL_SUBSTITUTE_TRIP_ID

    private val _posts: MutableLiveData<List<UiPostItem>> = MutableLiveData()
    val post: LiveData<List<UiPostItem>> = _posts

    private val _openTripDetailEvent = MutableLiveData<Event<Long>>()
    val openTripDetailEvent: LiveData<Event<Long>> = _openTripDetailEvent

    private val _openPostDetailEvent = MutableLiveData<Event<Long>>()
    val openPostDetailEvent: LiveData<Event<Long>> = _openPostDetailEvent

    private val _openDeleteDialogEvent = MutableLiveData<Event<Boolean>>()
    val openDeleteDialogEvent: LiveData<Event<Boolean>> = _openDeleteDialogEvent

    private val _deleteCompleteEvent = MutableLiveData<Boolean>()
    val deleteCompleteEvent: LiveData<Boolean> = _deleteCompleteEvent

    fun updatePreViewTrip(previewTrip: UiPreviewTrip) {
        _previewTrip.value = previewTrip
    }

    fun getPosts() {
        viewModelScope.launch {
            postRepository.getAllPosts(tripId)
                .onSuccess { posts ->
                    _posts.value = posts.map { post -> post.toPresentation() }
                }
                .onFailure {}
        }
    }

    fun deleteTrip() {
        viewModelScope.launch {
            tripRepository.deleteTrip(tripId)
                .onSuccess { _deleteCompleteEvent.value = true }
        }
    }

    fun openTripDetail() {
        _openTripDetailEvent.value = Event(tripId)
    }

    fun openPostDetail(postId: Long) {
        _openPostDetailEvent.value = Event(postId)
    }

    fun openDeleteDialog() {
        _openDeleteDialogEvent.value = Event(true)
    }
}

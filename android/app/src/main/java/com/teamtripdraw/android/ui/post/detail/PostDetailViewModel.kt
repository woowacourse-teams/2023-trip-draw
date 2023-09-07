package com.teamtripdraw.android.ui.post.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtripdraw.android.TripDrawApplication
import com.teamtripdraw.android.domain.constants.NULL_SUBSTITUTE_POST_ID
import com.teamtripdraw.android.domain.repository.PostRepository
import com.teamtripdraw.android.support.framework.presentation.event.Event
import com.teamtripdraw.android.ui.model.UiPostDetail
import com.teamtripdraw.android.ui.model.mapper.toDetailPresentation
import kotlinx.coroutines.launch

class PostDetailViewModel(
    private val repository: PostRepository,
) : ViewModel() {

    var postId: Long = NULL_SUBSTITUTE_POST_ID
        private set

    private val _postDetail: MutableLiveData<UiPostDetail> = MutableLiveData()
    val postDetail: LiveData<UiPostDetail> = _postDetail

    private val _openDeletionEvent = MutableLiveData<Event<Boolean>>()
    val openDeletionEvent: LiveData<Event<Boolean>> = _openDeletionEvent

    private val _postDeleteCompletedEvent: MutableLiveData<Boolean> = MutableLiveData()
    val postDeleteCompletedEvent: LiveData<Boolean> = _postDeleteCompletedEvent

    private val _editEvent: MutableLiveData<Event<Boolean>> = MutableLiveData(Event(false))
    val editEvent: LiveData<Event<Boolean>> = _editEvent

    fun initPostId(postId: Long) {
        this.postId = postId
    }

    fun fetchPost() {
        viewModelScope.launch {
            repository.getPost(postId)
                .onSuccess {
                    _postDetail.value = it.toDetailPresentation()
                }
                .onFailure {
                    // todo 오류 처리
                    TripDrawApplication.logUtil.general.log(it)
                }
        }
    }

    fun openDeletionEvent() {
        _openDeletionEvent.value = Event(true)
    }

    fun deletePost() {
        viewModelScope.launch {
            repository.deletePost(postId)
                .onSuccess {
                    _postDeleteCompletedEvent.value = true
                }
                .onFailure {
                    // todo 오류 처리
                    TripDrawApplication.logUtil.general.log(it)
                }
        }
    }

    fun editPost() {
        _editEvent.value = Event(true)
    }
}

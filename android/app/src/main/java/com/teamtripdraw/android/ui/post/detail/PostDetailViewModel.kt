package com.teamtripdraw.android.ui.post.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtripdraw.android.domain.repository.PostRepository
import com.teamtripdraw.android.support.framework.presentation.event.Event
import com.teamtripdraw.android.ui.model.UiPostDetail
import com.teamtripdraw.android.ui.model.mapper.toDetailPresentation
import kotlinx.coroutines.launch

class PostDetailViewModel(
    private val repository: PostRepository
) : ViewModel() {

    val postId: MutableLiveData<Long> = MutableLiveData()

    private val _postDetail: MutableLiveData<UiPostDetail> = MutableLiveData()
    val postDetail: LiveData<UiPostDetail> = _postDetail

    private val _postDeletedEvent: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val postDeletedEvent: LiveData<Event<Boolean>> = _postDeletedEvent

    fun getPostDetail() {
        viewModelScope.launch {
            repository.getPost(requireNotNull(postId.value))
                .onSuccess {
                    _postDetail.value = it.toDetailPresentation()
                }
                .onFailure {

                }
        }
    }

    fun deletePost() {
        viewModelScope.launch {
            repository.deletePost(requireNotNull(postId.value))
                .onSuccess {
                    _postDeletedEvent.value = Event(true)
                }
                .onFailure {

                }
        }
    }
}

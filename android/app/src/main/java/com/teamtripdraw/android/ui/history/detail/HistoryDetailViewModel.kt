package com.teamtripdraw.android.ui.history.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtripdraw.android.domain.repository.PostRepository
import com.teamtripdraw.android.ui.model.UiPostItem
import com.teamtripdraw.android.ui.model.UiPreviewTrip
import com.teamtripdraw.android.ui.model.mapper.toPresentation
import kotlinx.coroutines.launch

class HistoryDetailViewModel(
    private val postRepository: PostRepository,
) : ViewModel() {

    private val _previewTrip: MutableLiveData<UiPreviewTrip> = MutableLiveData()
    val previewTrip: LiveData<UiPreviewTrip> = _previewTrip

    private val _posts: MutableLiveData<List<UiPostItem>> = MutableLiveData()
    val post: LiveData<List<UiPostItem>> = _posts

    fun updatePreViewTrip(previewTrip: UiPreviewTrip) {
        _previewTrip.value = previewTrip
    }

    fun getPosts() {
        viewModelScope.launch {
            postRepository.getAllPosts(requireNotNull(previewTrip.value).id)
                .onSuccess { posts ->
                    _posts.value = posts.map { post -> post.toPresentation() }
                }
                .onFailure {
                }
        }
    }
}

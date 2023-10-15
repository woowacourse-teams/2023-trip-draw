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
import com.teamtripdraw.android.ui.model.allPosts.UiAllPosts
import com.teamtripdraw.android.ui.model.allPosts.UiItemView
import com.teamtripdraw.android.ui.model.allPosts.UiLoading
import com.teamtripdraw.android.ui.model.mapper.toPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllPostsViewModel @Inject constructor(
    private val postRepository: PostRepository,
) : ViewModel() {

    private val _uiPosts: MutableLiveData<List<UiItemView>> = MutableLiveData(listOf())
    val posts: LiveData<UiAllPosts> = Transformations.map(_uiPosts) { post -> UiAllPosts(post) }
    private val loadingItem = UiLoading()

    private val _openPostDetailEvent = MutableLiveData<Event<Long>>()
    val openPostDetailEvent: LiveData<Event<Long>> = _openPostDetailEvent

    private var lastId: Long? = null

    var hasNextPage = true
        private set

    var isAddLoading = false
        private set

    fun fetchPosts() {
        if (_uiPosts.value!!.isNotEmpty() && hasNextPage) {
            isAddLoading = true
            addLoadingItem()
        }
        getPosts()
    }

    private fun addLoadingItem() {
        _uiPosts.value = _uiPosts.value!!.toMutableList().apply { add(loadingItem) }
    }

    private fun getPosts() {
        viewModelScope.launch {
            postRepository.getAllPosts(lastViewedId = lastId, limit = PAGE_ITEM_SIZE)
                .onSuccess { posts ->
                    setLastItemId(posts)
                    setHasNextPage(posts)
                    isAddLoading = false
                    fetchPosts(posts)
                }
                .onFailure {
                    TripDrawApplication.logUtil.general.log(it)
                }
        }
    }

    private fun setLastItemId(posts: List<PostOfAll>) {
        if (posts.isNotEmpty()) lastId = posts.last().postId
    }

    private fun setHasNextPage(posts: List<PostOfAll>) {
        if (posts.size < PAGE_ITEM_SIZE && lastId != null) hasNextPage = false
    }

    private fun fetchPosts(posts: List<PostOfAll>) {
        _uiPosts.value = _uiPosts.value!!.toMutableList().apply {
            remove(loadingItem)
            addAll(posts.map { it.toPresentation() })
        }
    }

    fun openPostDetail(id: Long) {
        _openPostDetailEvent.value = Event(id)
    }

    companion object {
        private const val PAGE_ITEM_SIZE = 20
    }
}

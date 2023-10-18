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
import com.teamtripdraw.android.ui.filter.SelectedOptions
import com.teamtripdraw.android.ui.model.allPosts.UiAllPosts
import com.teamtripdraw.android.ui.model.allPosts.UiItemView
import com.teamtripdraw.android.ui.model.allPosts.UiLoadingItem
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

    var selectedOptions: SelectedOptions? = null

    private val _openPostDetailEvent = MutableLiveData<Event<Long>>()
    val openPostDetailEvent: LiveData<Event<Long>> = _openPostDetailEvent

    private val _openFilterSelectionEvent = MutableLiveData<Boolean>()
    val openFilterSelectionEvent: LiveData<Boolean> = _openFilterSelectionEvent

    private var lastId: Long? = null

    var hasNextPage = true
        private set

    var isAddLoading = false
        private set

    fun fetchPosts() {
        // 필터검색을 했다면 값을 처음부터 불러온다
        if (_openFilterSelectionEvent.value == true) {
            lastId = null
        }

        if (lastId != null && hasNextPage) {
            isAddLoading = true
            addLoadingItem()
        }
        getPosts()
    }

    private fun addLoadingItem() {
        _uiPosts.value = _uiPosts.value!!.toMutableList().apply { add(UiLoadingItem) }
    }

    private fun getPosts() {
        viewModelScope.launch {
            postRepository.getAllPosts(
                lastViewedId = lastId,
                limit = PAGE_ITEM_SIZE,
                address = selectedOptions?.address ?: "",
                years = selectedOptions?.years ?: listOf(),
                months = selectedOptions?.months ?: listOf(),
                daysOfWeek = selectedOptions?.daysOfWeek ?: listOf(),
                hours = selectedOptions?.hours ?: listOf(),
                ageRanges = selectedOptions?.ageRanges ?: listOf(),
                genders = selectedOptions?.genders ?: listOf(),
            ).onSuccess { posts ->
                setLastItemId(posts)
                setHasNextPage(posts)
                isAddLoading = false

                if (_openFilterSelectionEvent.value == true) {
                    getSearchResult(posts)
                } else {
                    fetchPosts(posts)
                }
            }.onFailure {
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

    private fun getSearchResult(posts: List<PostOfAll>) {
        _uiPosts.value = posts.map { it.toPresentation() }
        _openFilterSelectionEvent.value = false
    }

    private fun fetchPosts(posts: List<PostOfAll>) {
        _uiPosts.value = _uiPosts.value!!.toMutableList().apply {
            remove(UiLoadingItem)
            addAll(posts.map { it.toPresentation() })
        }
    }

    fun updateSelectedOptions(options: SelectedOptions) {
        selectedOptions = options
    }

    fun openPostDetail(id: Long) {
        _openPostDetailEvent.value = Event(id)
    }

    fun openFilterSelection() {
        _openFilterSelectionEvent.value = true
    }

    companion object {
        private const val PAGE_ITEM_SIZE = 20
    }
}

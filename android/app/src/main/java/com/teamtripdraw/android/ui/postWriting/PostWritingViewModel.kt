package com.teamtripdraw.android.ui.postWriting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teamtripdraw.android.domain.model.point.Point
import com.teamtripdraw.android.domain.model.post.PostWritingValidState
import com.teamtripdraw.android.domain.repository.PostRepository

class PostWritingViewModel(
    private val postRepository: PostRepository
) : ViewModel() {

    val MAX_INPUT_TITLE_LENGTH = PostWritingValidState.MAX_TITLE_LENGTH
    val MAX_INPUT_WRITING_LENGTH = PostWritingValidState.MAX_WRITING_LENGTH

    private var tripId: Long? = null
    private val _point: MutableLiveData<Point> = MutableLiveData()
    val point: LiveData<Point> = _point

    val title: MutableLiveData<String> = MutableLiveData("")
    val writing: MutableLiveData<String> = MutableLiveData("")

    private val _postWritingValidState: MutableLiveData<PostWritingValidState> =
        MutableLiveData(PostWritingValidState.EMPTY_TITLE)
    val postWritingValidState: LiveData<PostWritingValidState> = _postWritingValidState

    fun initData(tripId: Long, point: Point) {
        this.tripId = tripId
        _point.value = point
    }

    fun textChangedEvent() {
        _postWritingValidState.value =
            PostWritingValidState.getValidState(
                requireNotNull(title.value),
                requireNotNull(writing.value)
            )
    }

    fun completeEvent() {

    }

}
package com.teamtripdraw.android.ui.postWriting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teamtripdraw.android.domain.constants.NULL_SUBSTITUTE_POINT_ID
import com.teamtripdraw.android.domain.constants.NULL_SUBSTITUTE_TRIP_ID
import com.teamtripdraw.android.domain.model.post.PostWritingValidState
import com.teamtripdraw.android.domain.repository.PostRepository
import com.teamtripdraw.android.domain.repository.TripRepository

class PostWritingViewModel(
    private val tripRepository: TripRepository,
    private val postRepository: PostRepository
) : ViewModel() {

    val MAX_INPUT_TITLE_LENGTH = PostWritingValidState.MAX_TITLE_LENGTH
    val MAX_INPUT_WRITING_LENGTH = PostWritingValidState.MAX_WRITING_LENGTH

    private var tripId: Long = NULL_SUBSTITUTE_TRIP_ID
    private var pointId: Long = NULL_SUBSTITUTE_POINT_ID

    val title: MutableLiveData<String> = MutableLiveData("")
    val writing: MutableLiveData<String> = MutableLiveData("")

    private val _postWritingValidState: MutableLiveData<PostWritingValidState> =
        MutableLiveData(PostWritingValidState.EMPTY_TITLE)
    val postWritingValidState: LiveData<PostWritingValidState> = _postWritingValidState

    fun initData(tripId: Long, pointId: Long) {
        this.tripId = tripId
        this.pointId = pointId
    }

    fun textChangedEvent() {
        _postWritingValidState.value = PostWritingValidState.getValidState(
            requireNotNull(title.value), requireNotNull(writing.value)
        )
    }

    fun completeWritingEvent() {

    }

}
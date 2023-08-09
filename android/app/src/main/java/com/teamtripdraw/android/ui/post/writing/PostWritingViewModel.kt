package com.teamtripdraw.android.ui.post.writing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtripdraw.android.domain.constants.NULL_SUBSTITUTE_POINT_ID
import com.teamtripdraw.android.domain.constants.NULL_SUBSTITUTE_POST_ID
import com.teamtripdraw.android.domain.constants.NULL_SUBSTITUTE_TRIP_ID
import com.teamtripdraw.android.domain.model.point.Point
import com.teamtripdraw.android.domain.model.post.PostWritingValidState
import com.teamtripdraw.android.domain.model.post.PrePatchPost
import com.teamtripdraw.android.domain.model.post.PrePost
import com.teamtripdraw.android.domain.repository.PointRepository
import com.teamtripdraw.android.domain.repository.PostRepository
import com.teamtripdraw.android.domain.repository.TripRepository
import kotlinx.coroutines.launch
import java.io.File

class PostWritingViewModel(
    private val pointRepository: PointRepository,
    private val postRepository: PostRepository,
    private val tripRepository: TripRepository,
) : ViewModel() {

    val MAX_INPUT_TITLE_LENGTH = PostWritingValidState.MAX_TITLE_LENGTH
    val MAX_INPUT_WRITING_LENGTH = PostWritingValidState.MAX_WRITING_LENGTH

    private var tripId: Long = NULL_SUBSTITUTE_TRIP_ID
    private var pointId: Long = NULL_SUBSTITUTE_POINT_ID
    private var postId: Long = NULL_SUBSTITUTE_POST_ID
    private var imageFile: File? = null
    private lateinit var writingMode: WritingMode

    val title: MutableLiveData<String> = MutableLiveData("")
    val writing: MutableLiveData<String> = MutableLiveData("")

    private val _postWritingValidState: MutableLiveData<PostWritingValidState> =
        MutableLiveData(PostWritingValidState.EMPTY_TITLE)
    val postWritingValidState: LiveData<PostWritingValidState> = _postWritingValidState

    private val _writingCompletedEvent: MutableLiveData<Boolean> = MutableLiveData(false)
    val writingCompletedEvent: LiveData<Boolean> = _writingCompletedEvent

    private val _point: MutableLiveData<Point> = MutableLiveData()
    val point: LiveData<Point> = _point

    private val _address: MutableLiveData<String> = MutableLiveData("")
    val address: LiveData<String> = _address

    fun updateAddress(address: String) {
        _address.postValue(address)
    }

    fun updateImage(file: File) {
        this.imageFile = file
    }

    fun initWritingMode(writingMode: WritingMode, id: Long) {
        this.writingMode = writingMode
        when (writingMode) {
            WritingMode.NEW -> {
                pointId = id
                tripId = tripRepository.getCurrentTripId()
                fetchPoint()
            }
            WritingMode.EDIT -> {
                postId = id
                fetchPost()
            }
        }
    }

    fun textChangedEvent() {
        _postWritingValidState.value = PostWritingValidState.getValidState(
            requireNotNull(title.value),
            requireNotNull(writing.value),
        )
    }

    fun completeWritingEvent() {
        viewModelScope.launch {
            when (writingMode) {
                WritingMode.NEW -> {
                    val prePost = PrePost(
                        tripId = tripId,
                        pointId = pointId,
                        title = title.value ?: "",
                        writing = writing.value ?: "",
                        address = address.value ?: "",
                        imageFile = imageFile,
                    )
                    postRepository.addPost(prePost).onSuccess {
                        _writingCompletedEvent.value = true
                    }
                }
                WritingMode.EDIT -> {
                    val prePatchPost = PrePatchPost(
                        postId = postId,
                        title = title.value ?: "",
                        writing = writing.value ?: "",
                        imageFile = imageFile,
                    )
                    postRepository.patchPost(prePatchPost).onSuccess {
                        _writingCompletedEvent.value = true
                    }
                }
            }
        }
    }

    private fun fetchPoint() {
        viewModelScope.launch {
            pointRepository.getPoint(pointId = pointId, tripId = tripId)
                .onSuccess { _point.value = it }
        }
    }

    private fun fetchPost() {
        viewModelScope.launch {
            postRepository.getPost(postId = postId)
                .onSuccess {
                    _address.value = it.address
                    title.value = it.title
                    writing.value = it.writing
                }
        }
    }
}

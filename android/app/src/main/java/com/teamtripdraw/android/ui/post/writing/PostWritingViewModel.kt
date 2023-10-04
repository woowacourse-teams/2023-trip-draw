package com.teamtripdraw.android.ui.post.writing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtripdraw.android.domain.model.point.Point
import com.teamtripdraw.android.domain.model.post.Post
import com.teamtripdraw.android.domain.model.post.PostWritingValidState
import com.teamtripdraw.android.domain.model.post.PrePatchPost
import com.teamtripdraw.android.domain.model.post.PrePost
import com.teamtripdraw.android.domain.model.trip.Trip
import com.teamtripdraw.android.domain.repository.GeocodingRepository
import com.teamtripdraw.android.domain.repository.PointRepository
import com.teamtripdraw.android.domain.repository.PostRepository
import com.teamtripdraw.android.domain.repository.TripRepository
import com.teamtripdraw.android.support.framework.presentation.event.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PostWritingViewModel @Inject constructor(
    private val pointRepository: PointRepository,
    private val postRepository: PostRepository,
    private val tripRepository: TripRepository,
    private val geocodingRepository: GeocodingRepository,
) : ViewModel() {

    val MAX_INPUT_TITLE_LENGTH = PostWritingValidState.MAX_TITLE_LENGTH
    val MAX_INPUT_WRITING_LENGTH = PostWritingValidState.MAX_WRITING_LENGTH

    private var tripId: Long = Trip.NULL_SUBSTITUTE_ID
    private var pointId: Long = Point.NULL_SUBSTITUTE_ID
    private var postId: Long = Post.NULL_SUBSTITUTE_ID
    private lateinit var writingMode: WritingMode

    val title: MutableLiveData<String> = MutableLiveData("")
    val writing: MutableLiveData<String> = MutableLiveData("")

    private val _postWritingValidState: MutableLiveData<PostWritingValidState> =
        MutableLiveData(PostWritingValidState.EMPTY_TITLE)
    val postWritingValidState: LiveData<PostWritingValidState> = _postWritingValidState

    private val _backPageEvent: MutableLiveData<Event<Boolean>> = MutableLiveData(Event(false))
    val backPageEvent: LiveData<Event<Boolean>> = _backPageEvent

    private val _writingCompletedEvent: MutableLiveData<Event<Boolean>> =
        MutableLiveData(Event(false))
    val writingCompletedEvent: LiveData<Event<Boolean>> = _writingCompletedEvent

    private val _point: MutableLiveData<Point> = MutableLiveData()
    val point: LiveData<Point> = _point

    private val _address: MutableLiveData<String> = MutableLiveData("")
    val address: LiveData<String> = _address

    // 기기에서 선택된 이미지가 저장되며, 서버에 이미지 저장을 요청할 때 이용됩니다.
    private var imageFile: MutableLiveData<File?> = MutableLiveData()

    // 기기에서 선택된 이미지 파일의 uri 또는 서버로부터 받아온 image uri가 저장됩니다. view에 표시될 때 이용됩니다.
    private val _imageUri: MutableLiveData<String?> = MutableLiveData(null)
    val imageUri: LiveData<String?> = _imageUri

    val hasImage: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        this.addSource(imageUri) { value -> this.setValue(value?.isNotBlank()) }
        this.addSource(imageFile) { value -> this.setValue(value != null) }
    }

    private val _takePictureEvent: MutableLiveData<Boolean> = MutableLiveData(false)
    val takePictureEvent: LiveData<Boolean> = _takePictureEvent

    private val _selectPhotoEvent: MutableLiveData<Boolean> = MutableLiveData(false)
    val selectPhotoEvent: LiveData<Boolean> = _selectPhotoEvent

    fun updateImage(file: File) {
        imageFile.value = file
        _imageUri.value = file.toURI().toString()
    }

    fun deleteImage() {
        imageFile.value = null
        _imageUri.value = null
    }

    fun backPage() {
        _backPageEvent.value = Event(true)
    }

    fun takePicture() {
        _takePictureEvent.value = true
        _takePictureEvent.value = false
    }

    fun selectPhoto() {
        _selectPhotoEvent.value = true
        _selectPhotoEvent.value = false
    }

    fun initPostMetaData(tripId: Long, pointId: Long, writingMode: WritingMode = WritingMode.NEW) {
        this.writingMode = writingMode
        this.tripId = tripId
        this.pointId = pointId
        fetchPoint()
    }

    fun initPostMetaData(postId: Long, writingMode: WritingMode = WritingMode.EDIT) {
        this.writingMode = writingMode
        this.postId = postId
        fetchPost()
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
                        imageFile = imageFile.value,
                    )
                    postRepository.addPost(prePost).onSuccess {
                        _writingCompletedEvent.value = Event(true)
                    }
                }
                WritingMode.EDIT -> {
                    val prePatchPost = PrePatchPost(
                        postId = postId,
                        title = title.value ?: "",
                        writing = writing.value ?: "",
                        imageFile = imageFile.value,
                    )
                    postRepository.patchPost(prePatchPost).onSuccess {
                        _writingCompletedEvent.value = Event(true)
                    }
                }
            }
        }
    }

    private fun fetchPoint() {
        viewModelScope.launch {
            pointRepository.getPoint(pointId = pointId, tripId = tripId)
                .onSuccess {
                    _point.value = it
                    fetchAddress()
                }
        }
    }

    private fun fetchPost() {
        viewModelScope.launch {
            postRepository.getPost(postId = postId)
                .onSuccess {
                    _address.value = it.address
                    title.value = it.title
                    writing.value = it.writing
                    _imageUri.value = it.postImageUrl
                }
        }
    }

    private fun fetchAddress() {
        _point.value?.let { point ->
            viewModelScope.launch {
                geocodingRepository.getAddress(point.latitude, point.longitude)
                    .onSuccess { _address.value = it }
            }
        }
    }
}

package com.teamtripdraw.android.ui.postWriting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtripdraw.android.domain.constants.NULL_SUBSTITUTE_POINT_ID
import com.teamtripdraw.android.domain.constants.NULL_SUBSTITUTE_TRIP_ID
import com.teamtripdraw.android.domain.model.point.LatLngPoint
import com.teamtripdraw.android.domain.model.post.PostWritingValidState
import com.teamtripdraw.android.domain.repository.PointRepository
import com.teamtripdraw.android.domain.repository.PostRepository
import com.teamtripdraw.android.domain.repository.TripRepository
import kotlinx.coroutines.launch

class PostWritingViewModel(
    private val pointRepository: PointRepository,
    private val postRepository: PostRepository,
    private val tripRepository: TripRepository
) : ViewModel() {

    val MAX_INPUT_TITLE_LENGTH = PostWritingValidState.MAX_TITLE_LENGTH
    val MAX_INPUT_WRITING_LENGTH = PostWritingValidState.MAX_WRITING_LENGTH

    private var tripId: Long = NULL_SUBSTITUTE_TRIP_ID
    private var pointId: Long = NULL_SUBSTITUTE_POINT_ID

    private val _latLngPoint: MutableLiveData<LatLngPoint> = MutableLiveData(LatLngPoint(0.0, 0.0))
    val latLngPoint: LiveData<LatLngPoint> = _latLngPoint

    val title: MutableLiveData<String> = MutableLiveData("")
    val writing: MutableLiveData<String> = MutableLiveData("")

    private val _postWritingValidState: MutableLiveData<PostWritingValidState> =
        MutableLiveData(PostWritingValidState.EMPTY_TITLE)
    val postWritingValidState: LiveData<PostWritingValidState> = _postWritingValidState

    fun initTripData(pointId: Long) {
        this.tripId = tripRepository.getCurrentTripId()
        this.pointId = pointId
        viewModelScope.launch {
            pointRepository.getPoint(pointId = pointId, tripId = tripId)
                .onSuccess { _latLngPoint.value = LatLngPoint(it.latitude, it.longitude) }
        }
    }

    fun textChangedEvent() {
        _postWritingValidState.value = PostWritingValidState.getValidState(
            requireNotNull(title.value), requireNotNull(writing.value)
        )
    }

    fun completeWritingEvent() {

    }

}
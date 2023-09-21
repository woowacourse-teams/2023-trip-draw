package com.teamtripdraw.android.ui.home.markerSelectedBottomSheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtripdraw.android.domain.model.point.Point
import com.teamtripdraw.android.domain.model.trip.Trip
import com.teamtripdraw.android.domain.repository.GeocodingRepository
import com.teamtripdraw.android.domain.repository.PointRepository
import com.teamtripdraw.android.support.framework.presentation.event.Event
import com.teamtripdraw.android.ui.model.UiPoint
import com.teamtripdraw.android.ui.model.mapper.toPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class MarkerSelectedViewModel @Inject constructor(
    private val pointRepository: PointRepository,
    private val geocodingRepository: GeocodingRepository,
) : ViewModel() {

    private var pointId by Delegates.notNull<Long>()

    private var tripId: Long = Trip.NULL_SUBSTITUTE_ID

    private val _selectedPoint = MutableLiveData<Point>()
    val selectedUiPoint: LiveData<UiPoint> = Transformations.map(_selectedPoint) { point ->
        point.toPresentation()
    }

    private val _address = MutableLiveData<String>()
    val address: LiveData<String> = _address

    private val _openPostWritingEvent = MutableLiveData<Event<Long>>()
    val openPostWritingEvent: LiveData<Event<Long>> = _openPostWritingEvent

    private val _deletePointEvent = MutableLiveData<Event<Boolean>>()
    val deletePointEvent: LiveData<Event<Boolean>> = _deletePointEvent

    fun updatePointId(pointId: Long) {
        this.pointId = pointId
    }

    fun updateTripId(tripId: Long) {
        this.tripId = tripId
    }

    fun getPointInfo() {
        viewModelScope.launch {
            pointRepository.getPoint(pointId, tripId).onSuccess {
                _selectedPoint.value = it
                fetchAddress()
            }
        }
    }

    fun openPostWriting() {
        _openPostWritingEvent.value = Event(pointId)
    }

    fun deletePoint() {
        viewModelScope.launch {
            pointRepository.deletePoint(tripId = tripId, pointId = pointId).onSuccess {
                _deletePointEvent.value = Event(true)
            }.onFailure {
                // todo log전략 수립후 서버로 전송되는 로그 찍기
            }
        }
    }

    private fun fetchAddress() {
        _selectedPoint.value?.let { point ->
            viewModelScope.launch {
                geocodingRepository.getAddress(point.latitude, point.longitude)
                    .onSuccess { _address.value = it }
            }
        }
    }
}

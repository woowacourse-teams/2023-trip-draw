package com.teamtripdraw.android.ui.home.markerSelectedBottomSheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtripdraw.android.domain.model.point.Point
import com.teamtripdraw.android.domain.repository.PointRepository
import com.teamtripdraw.android.domain.repository.TripRepository
import com.teamtripdraw.android.support.framework.presentation.event.Event
import com.teamtripdraw.android.ui.model.UiPoint
import com.teamtripdraw.android.ui.model.mapper.toPresentation
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class MarkerSelectedViewModel(
    private val tripRepository: TripRepository,
    private val pointRepository: PointRepository
) : ViewModel() {

    private var pointId by Delegates.notNull<Long>()

    private val tripId: Long
        get() = tripRepository.getCurrentTripId()

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

    fun getPointInfo() {
        viewModelScope.launch {
            pointRepository.getPoint(pointId, tripId).onSuccess { _selectedPoint.value = it }
        }
    }

    fun updateAddress(address: String) {
        _address.postValue(address)
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
}
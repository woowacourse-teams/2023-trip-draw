package com.teamtripdraw.android.ui.history.tripDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationResult
import com.teamtripdraw.android.domain.model.point.PrePoint
import com.teamtripdraw.android.domain.repository.PointRepository
import com.teamtripdraw.android.domain.repository.TripRepository
import com.teamtripdraw.android.support.framework.presentation.event.Event
import com.teamtripdraw.android.ui.model.UiRoute
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class TripDetailViewModel(
    private val tripRepository: TripRepository,
    private val pointRepository: PointRepository
) : ViewModel() {

    private val _tripId = MutableLiveData<Long>()
    val tripId: LiveData<Long> = _tripId

    private val _currentTripRoute = MutableLiveData<UiRoute>()
    val currentTripRoute: LiveData<UiRoute> = _currentTripRoute

    private val _openPostViewerEvent = MutableLiveData<Event<Boolean>>()
    val openPostViewerEvent: LiveData<Event<Boolean>> = _openPostViewerEvent

    private val _openPostWritingEvent = MutableLiveData<Event<Long>>()
    val openPostWritingEvent: LiveData<Event<Long>> = _openPostWritingEvent

    private val _markerViewModeState = MutableLiveData<Boolean>(false)
    val markerViewModeState: LiveData<Boolean> = _markerViewModeState
    val markerViewModeStateValue: Boolean
        get() = markerViewModeState.value ?: false

    fun updateTripId(id: Long) {
        _tripId.value = id
    }

    fun toggleMarkerViewModeState() {
        _markerViewModeState.value = !requireNotNull(markerViewModeState.value)
    }

    fun openPostViewer() {
        _openPostViewerEvent.value = Event(true)
    }

    fun createPoint(locationResult: LocationResult) {
        viewModelScope.launch {
            pointRepository.createRecordingPoint(
                getPrePoint(locationResult),
                tripId.value
            ).onSuccess {
                _openPostWritingEvent.value = Event(it)
            }.onFailure {
                // todo log전략 수립후 서버로 전송되는 로그 찍기
            }
        }
    }

    private fun getPrePoint(locationResult: LocationResult): PrePoint {
        return PrePoint(
            locationResult.locations.first().latitude,
            locationResult.locations.first().longitude,
            LocalDateTime.now()
        )
    }
}

package com.teamtripdraw.android.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationResult
import com.teamtripdraw.android.domain.constants.NULL_SUBSTITUTE_TRIP_ID
import com.teamtripdraw.android.domain.model.point.PrePoint
import com.teamtripdraw.android.domain.model.point.Route
import com.teamtripdraw.android.domain.repository.PointRepository
import com.teamtripdraw.android.domain.repository.TripRepository
import com.teamtripdraw.android.support.framework.presentation.event.Event
import com.teamtripdraw.android.ui.model.UiRoute
import com.teamtripdraw.android.ui.model.mapper.toPresentation
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class HomeViewModel(
    private val tripRepository: TripRepository,
    private val pointRepository: PointRepository
) : ViewModel() {

    private val _homeUiState: MutableLiveData<HomeUiState> = MutableLiveData()
    val homeUiState: LiveData<HomeUiState> = _homeUiState

    private val _startTripEvent = MutableLiveData<Event<Boolean>>()
    val startTripEvent: LiveData<Event<Boolean>> = _startTripEvent

    private val _currentTripRoute = MutableLiveData<Route>()
    val currentTripUiRoute: LiveData<UiRoute> =
        Transformations.map(_currentTripRoute) { route -> route.toPresentation() }

    private val _markerViewModeState = MutableLiveData<Boolean>(false)
    val markerViewModeState: LiveData<Boolean> = _markerViewModeState

    val markerViewModeStateValue: Boolean
        get() = markerViewModeState.value ?: false

    private val _openPostViewerEvent = MutableLiveData<Event<Boolean>>()
    val openPostViewerEvent: LiveData<Event<Boolean>> = _openPostViewerEvent

    private val _openPostWritingEvent = MutableLiveData<Event<Long>>()
    val openPostWritingEvent: LiveData<Event<Long>> = _openPostWritingEvent

    private val _markerSelectEvent = MutableLiveData<Long>()
    val makerSelectedEvent: LiveData<Long> = _markerSelectEvent

    val notificationMarkerSelected: (pointId: Long) -> Unit = { _markerSelectEvent.value = it }

    private val _finishTripEvent = MutableLiveData<Boolean>()
    val finishTripEvent: LiveData<Boolean> = _finishTripEvent

    var tripId: Long = NULL_SUBSTITUTE_TRIP_ID
        private set

    var markerSelectedState: Boolean = false

    init {
        updateTripId()
        initHomeUiState()
        updateCurrentTripRoute()
    }

    private fun initHomeUiState() {
        _homeUiState.value = when (tripId) {
            NULL_SUBSTITUTE_TRIP_ID -> HomeUiState.BEFORE_TRIP
            else -> HomeUiState.ON_TRIP
        }
    }

    private fun updateTripId() {
        tripId = tripRepository.getCurrentTripId()
    }

    fun startTrip() {
        viewModelScope.launch {
            tripRepository.startTrip()
                .onSuccess {
                    updateTripId()
                    _homeUiState.value = HomeUiState.ON_TRIP
                    _startTripEvent.value = Event(true)
                }
        }
    }

    fun updateCurrentTripRoute() {
        if (tripId == NULL_SUBSTITUTE_TRIP_ID) return
        viewModelScope.launch {
            tripRepository.getCurrentTripRoute(tripId)
                .onSuccess {
                    _currentTripRoute.value = it
                }
        }
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
                tripId
            ).onSuccess {
                _openPostWritingEvent.value = Event(it)
            }.onFailure {
                // todo log전략 수립후 서버로 전송되는 로그 찍기
            }
        }
    }

    private fun getPrePoint(locationResult: LocationResult): PrePoint =
        PrePoint(
            locationResult.locations.first().latitude,
            locationResult.locations.first().longitude,
            LocalDateTime.now()
        )

    fun finishTripEvent() {
        _finishTripEvent.value = true
    }

    fun resetFinishTripEvent() {
        _finishTripEvent.value = false
    }
}

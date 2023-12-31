package com.teamtripdraw.android.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationResult
import com.teamtripdraw.android.domain.model.point.Route
import com.teamtripdraw.android.domain.model.trip.Trip
import com.teamtripdraw.android.domain.repository.PointRepository
import com.teamtripdraw.android.domain.repository.TripRepository
import com.teamtripdraw.android.support.framework.presentation.event.Event
import com.teamtripdraw.android.ui.home.markerSelectedBottomSheet.MapBottomSheetViewModel
import com.teamtripdraw.android.ui.model.UiRoute
import com.teamtripdraw.android.ui.model.mapper.toPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val tripRepository: TripRepository,
    private val pointRepository: PointRepository,
) : ViewModel(), MapBottomSheetViewModel {

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

    private val _openPostWritingEvent = MutableLiveData<Event<LocationResult>>()
    val openPostWritingEvent: LiveData<Event<LocationResult>> = _openPostWritingEvent

    private val _markerSelectEvent = MutableLiveData<Long>()
    val makerSelectedEvent: LiveData<Long> = _markerSelectEvent

    val notificationMarkerSelected: (pointId: Long) -> Unit = { _markerSelectEvent.value = it }

    private val _finishTripEvent = MutableLiveData<Boolean>()
    val finishTripEvent: LiveData<Boolean> = _finishTripEvent

    private val _finishTripCompleteEvent = MutableLiveData<Boolean>()
    val finishTripCompleteEvent: LiveData<Boolean> = _finishTripCompleteEvent

    var tripId: Long = Trip.NULL_SUBSTITUTE_ID
        private set

    override var markerSelectedState: Boolean = false

    init {
        updateTripId()
        initHomeUiState()
        updateTripInfo()
    }

    private fun initHomeUiState() {
        updateHomeUiState(
            when (tripId) {
                Trip.NULL_SUBSTITUTE_ID -> HomeUiState.BEFORE_TRIP
                else -> HomeUiState.ON_TRIP
            },
        )
    }

    private fun updateTripId() {
        tripId = tripRepository.getCurrentTripId()
    }

    fun startTrip() {
        viewModelScope.launch {
            tripRepository.startTrip()
                .onSuccess {
                    updateTripId()
                    updateHomeUiState(HomeUiState.ON_TRIP)
                    _startTripEvent.value = Event(true)
                }
        }
    }

    fun updateHomeUiState(homeUiState: HomeUiState) {
        _homeUiState.value = homeUiState
    }

    override fun updateTripInfo() {
        if (tripId == Trip.NULL_SUBSTITUTE_ID) return
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

    fun openPostWriting(locationResult: LocationResult) {
        _openPostWritingEvent.value = Event(locationResult)
    }

    fun finishTripEvent() {
        _finishTripEvent.value = true
    }

    fun resetFinishTripEvent() {
        _finishTripEvent.value = false
    }

    fun finishTripCompleteEvent() {
        _finishTripCompleteEvent.value = true
    }

    fun resetFinishTripCompleteEvent() {
        _finishTripCompleteEvent.value = false
    }

    fun clearCurrentTripId() {
        tripRepository.deleteCurrentTripId()
    }

    fun cleatCurrentTripRoute() {
        _currentTripRoute.value = Route(listOf())
    }
}

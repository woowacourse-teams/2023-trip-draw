package com.teamtripdraw.android.ui.history.tripDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtripdraw.android.TripDrawApplication
import com.teamtripdraw.android.domain.model.point.Route
import com.teamtripdraw.android.domain.model.trip.Trip
import com.teamtripdraw.android.domain.repository.TripRepository
import com.teamtripdraw.android.support.framework.presentation.event.Event
import com.teamtripdraw.android.ui.home.markerSelectedBottomSheet.MapBottomSheetViewModel
import com.teamtripdraw.android.ui.model.UiRoute
import com.teamtripdraw.android.ui.model.mapper.toPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripDetailViewModel @Inject constructor(
    private val tripRepository: TripRepository,
) : ViewModel(), MapBottomSheetViewModel {

    private val _tripRoute = MutableLiveData<Route>()
    val tripRoute: LiveData<UiRoute> =
        Transformations.map(_tripRoute) { route -> route.toPresentation() }

    private val _tripTitle = MutableLiveData<String>()
    val tripTitle: LiveData<String> = _tripTitle

    private val _markerViewModeState = MutableLiveData<Boolean>(false)
    val markerViewModeState: LiveData<Boolean> = _markerViewModeState

    val markerViewModeStateValue: Boolean
        get() = markerViewModeState.value ?: false

    private val _openPostViewerEvent = MutableLiveData<Event<Boolean>>()
    val openPostViewerEvent: LiveData<Event<Boolean>> = _openPostViewerEvent

    private val _markerSelectEvent = MutableLiveData<Long>()
    val makerSelectedEvent: LiveData<Long> = _markerSelectEvent

    val notificationMarkerSelected: (pointId: Long) -> Unit = { _markerSelectEvent.value = it }

    var tripId: Long = Trip.NULL_SUBSTITUTE_ID
        private set

    override var markerSelectedState: Boolean = false

    fun updateTripId(tripId: Long) {
        this.tripId = tripId
    }

    override fun updateTripInfo() {
        if (tripId == Trip.NULL_SUBSTITUTE_ID) return
        viewModelScope.launch {
            tripRepository.getTrip(tripId)
                .onSuccess {
                    _tripRoute.value = it.route
                    _tripTitle.value = it.name
                }
                .onFailure {
                    TripDrawApplication.logUtil.general.log(it)
                }
        }
    }

    fun toggleMarkerViewModeState() {
        _markerViewModeState.value = !requireNotNull(markerViewModeState.value)
    }

    fun openPostViewer() {
        _openPostViewerEvent.value = Event(true)
    }
}

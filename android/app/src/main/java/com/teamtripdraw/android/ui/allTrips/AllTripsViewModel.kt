package com.teamtripdraw.android.ui.allTrips

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtripdraw.android.TripDrawApplication
import com.teamtripdraw.android.domain.repository.TripRepository
import com.teamtripdraw.android.ui.model.UiPreviewTrip
import com.teamtripdraw.android.ui.model.allTrips.UiAllTripItem
import com.teamtripdraw.android.ui.model.allTrips.UiAllTrips
import com.teamtripdraw.android.ui.model.allTrips.UiTripOfAll
import com.teamtripdraw.android.ui.model.mapper.toPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllTripsViewModel @Inject constructor(
    private val tripRepository: TripRepository,
) : ViewModel() {

    private val _trips: MutableLiveData<List<UiAllTripItem>> = MutableLiveData()
    val trips: LiveData<UiAllTrips> = Transformations.map(_trips) { trip -> UiAllTrips(trip) }

    private val _openHistoryDetailEvent = MutableLiveData<UiPreviewTrip>()
    val openHistoryDetailEvent: LiveData<UiPreviewTrip> = _openHistoryDetailEvent

    fun fetchTrips() {
        viewModelScope.launch {
            tripRepository.getAllTrips()
                .onSuccess { it ->
                    _trips.value = it.map { it.toPresentation() }
                }
                .onFailure {
                    TripDrawApplication.logUtil.general.log(it)
                }
        }
    }

    fun openHistoryDetail(trip: UiTripOfAll) {
        val previewTrip = UiPreviewTrip(
            id = trip.tripId,
            name = trip.name,
            imageUrl = trip.imageUrl,
            routeImageUrl = trip.routeImageUrl,
        )
        _openHistoryDetailEvent.value = previewTrip
    }
}

package com.teamtripdraw.android.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtripdraw.android.TripDrawApplication
import com.teamtripdraw.android.domain.model.trip.PreviewTrip
import com.teamtripdraw.android.domain.repository.TripRepository
import com.teamtripdraw.android.support.framework.presentation.event.Event
import com.teamtripdraw.android.ui.model.UiPreviewTrip
import com.teamtripdraw.android.ui.model.UiTripHistory
import com.teamtripdraw.android.ui.model.mapper.toPresentation
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val tripRepository: TripRepository,
) : ViewModel() {

    private val _previewTrips: MutableLiveData<List<PreviewTrip>> = MutableLiveData()
    val previewTrips: LiveData<UiTripHistory> =
        Transformations.map(_previewTrips) { trip -> UiTripHistory(trip.map { it.toPresentation() }) }

    private val _previewTripOpenEvent = MutableLiveData<Event<UiPreviewTrip>>()
    val previewTripOpenEvent: LiveData<Event<UiPreviewTrip>> = _previewTripOpenEvent

    fun fetchPreviewTrips() {
        viewModelScope.launch {
            tripRepository.getAllTrips()
                .onSuccess {
                    _previewTrips.value = it
                }
                .onFailure {
                    TripDrawApplication.logUtil.general.log(it)
                }
        }
    }

    fun openHistoryDetail(previewTrip: UiPreviewTrip) {
        _previewTripOpenEvent.value = Event(previewTrip)
    }
}

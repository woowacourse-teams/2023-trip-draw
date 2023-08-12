package com.teamtripdraw.android.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtripdraw.android.domain.model.trip.PreviewTrip
import com.teamtripdraw.android.domain.repository.TripRepository
import com.teamtripdraw.android.support.framework.presentation.event.Event
import com.teamtripdraw.android.ui.model.UiPreviewTrip
import com.teamtripdraw.android.ui.model.mapper.toPresentation
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val tripRepository: TripRepository,
) : ViewModel() {

    private val _previewTrips: MutableLiveData<List<PreviewTrip>> = MutableLiveData()
    val previewTrips: LiveData<List<UiPreviewTrip>> =
        Transformations.map(_previewTrips) { trip -> trip.map { it.toPresentation() } }

    private val _noTripMessageEvent = MutableLiveData<Boolean>()
    val noTripMessageEvent: LiveData<Boolean> = _noTripMessageEvent

    private val _previewTripOpenEvent = MutableLiveData<Event<UiPreviewTrip>>()
    val previewTripOpenEvent: LiveData<Event<UiPreviewTrip>> = _previewTripOpenEvent

    fun getPreviewTrips() {
        viewModelScope.launch {
            tripRepository.getAllTrips()
                .onSuccess {
                    _previewTrips.value = it
                    _noTripMessageEvent.value = it.isEmpty()
                }
                .onFailure { }
        }
    }

    fun openHistoryDetail(previewTrip: UiPreviewTrip) {
        _previewTripOpenEvent.value = Event(previewTrip)
    }
}

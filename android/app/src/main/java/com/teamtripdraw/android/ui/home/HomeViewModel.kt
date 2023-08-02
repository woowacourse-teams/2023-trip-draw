package com.teamtripdraw.android.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtripdraw.android.domain.constants.NULL_SUBSTITUTE_TRIP_ID
import com.teamtripdraw.android.domain.repository.TripRepository
import com.teamtripdraw.android.support.framework.presentation.event.Event
import kotlinx.coroutines.launch

class HomeViewModel(
    private val tripRepository: TripRepository
) : ViewModel() {

    private val _homeUiState: MutableLiveData<HomeUiState> = MutableLiveData()
    val homeUiState: LiveData<HomeUiState> = _homeUiState

    private val _startTripEvent = MutableLiveData<Event<Boolean>>()
    val startTripEvent: LiveData<Event<Boolean>> = _startTripEvent

    var tripId: Long = NULL_SUBSTITUTE_TRIP_ID
        private set

    init {
        initHomeUiState()
        updateTripId()
    }

    private fun initHomeUiState() {
        _homeUiState.value = when (tripRepository.getCurrentTripId()) {
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
}

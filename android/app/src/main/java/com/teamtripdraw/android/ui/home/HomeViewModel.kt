package com.teamtripdraw.android.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtripdraw.android.domain.repository.TripRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val tripRepository: TripRepository
) : ViewModel() {

    private val _homeUiState: MutableLiveData<HomeUiState> = MutableLiveData()
    val homeUiState: LiveData<HomeUiState> = _homeUiState

    init {
        _homeUiState.value = when (tripRepository.getStoredTripId()) {
            -1L -> HomeUiState.BEFORE_TRIP
            else -> HomeUiState.ON_TRIP
        }
    }

    fun startTrip() {
        viewModelScope.launch {
            tripRepository.startTrip()
                .onSuccess {
                    _homeUiState.value = HomeUiState.ON_TRIP
                }
        }
    }
}

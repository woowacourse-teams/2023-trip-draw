package com.teamtripdraw.android.ui.allTrips

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamtripdraw.android.TripDrawApplication
import com.teamtripdraw.android.domain.model.trip.TripOfAll
import com.teamtripdraw.android.domain.repository.TripRepository
import com.teamtripdraw.android.ui.model.UiAllTrips
import com.teamtripdraw.android.ui.model.mapper.toPresentation
import kotlinx.coroutines.launch

class AllTripsViewModel(
    private val tripRepository: TripRepository,
) : ViewModel() {
    private val _trips: MutableLiveData<List<TripOfAll>> = MutableLiveData()
    val trips: LiveData<UiAllTrips> =
        Transformations.map(_trips) { trip -> UiAllTrips(trip.map { it.toPresentation() }) }

    fun fetchTrips() {
        viewModelScope.launch {
            tripRepository.getAllTrips()
                .onSuccess {
                    _trips.value = it
                }
                .onFailure {
                    TripDrawApplication.logUtil.general.log(it)
                }
        }
    }
}

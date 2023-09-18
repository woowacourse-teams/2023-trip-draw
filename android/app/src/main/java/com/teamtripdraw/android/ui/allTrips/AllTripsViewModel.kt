package com.teamtripdraw.android.ui.allTrips

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.teamtripdraw.android.domain.model.trip.TripOfAll
import com.teamtripdraw.android.ui.model.UiAllTrips
import com.teamtripdraw.android.ui.model.mapper.toPresentation
import java.time.LocalDateTime

class AllTripsViewModel : ViewModel() {
    private val _trips: MutableLiveData<List<TripOfAll>> = MutableLiveData()
    val trips: LiveData<UiAllTrips> =
        Transformations.map(_trips) { trip -> UiAllTrips(trip.map { it.toPresentation() }) }

    fun fetchTrips() {
        // dummy data 입니다
        _trips.value = List(100) {
            TripOfAll(
                tripId = 1134,
                name = "아뮤지컬또보고싶다진짜너무비싼데또보고싶다",
                imageUrl = "https://img.freepik.com/free-photo/world-smile-day-emojis-arrangement_23-2149024491.jpg?q=10&h=200",
                routeImageUrl = "https://blog.kakaocdn.net/dn/bezjux/btqCX8fuOPX/6uq138en4osoKRq9rtbEG0/img.jpg",
                startTime = LocalDateTime.of(2023, 9, 18, 14, 45),
                endTime = LocalDateTime.of(2023, 10, 18, 14, 45),
            )
        }
    }
}

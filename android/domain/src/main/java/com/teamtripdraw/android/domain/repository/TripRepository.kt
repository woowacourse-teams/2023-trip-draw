package com.teamtripdraw.android.domain.repository

import com.teamtripdraw.android.domain.model.point.Route
import com.teamtripdraw.android.domain.model.trip.PreSetTripTitle
import com.teamtripdraw.android.domain.model.trip.PreviewTrip
import com.teamtripdraw.android.domain.model.trip.Trip

interface TripRepository {
    suspend fun startTrip(): Result<Unit>
    fun getCurrentTripId(): Long
    fun deleteCurrentTripId()
    suspend fun getCurrentTripRoute(tripId: Long): Result<Route>
    suspend fun getTrip(tripId: Long): Result<Trip>
    suspend fun setTripTitle(tripId: Long, preSetTripTitle: PreSetTripTitle): Result<Unit>
    suspend fun getMyTrips(): Result<List<PreviewTrip>>
    suspend fun deleteTrip(tripId: Long): Result<Unit>
}

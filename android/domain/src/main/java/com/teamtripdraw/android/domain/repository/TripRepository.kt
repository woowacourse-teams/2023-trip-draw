package com.teamtripdraw.android.domain.repository

import com.teamtripdraw.android.domain.model.point.Route
import com.teamtripdraw.android.domain.model.trip.PreSetTripTitle
import com.teamtripdraw.android.domain.model.trip.PreviewTrip

interface TripRepository {
    suspend fun startTrip(): Result<Unit>
    fun getCurrentTripId(): Long
    suspend fun getCurrentTripRoute(tripId: Long): Result<Route>
    suspend fun setTripTitle(tripId: Long, preSetTripTitle: PreSetTripTitle): Result<Unit>
    suspend fun getAllTrips(): Result<List<PreviewTrip>>
}

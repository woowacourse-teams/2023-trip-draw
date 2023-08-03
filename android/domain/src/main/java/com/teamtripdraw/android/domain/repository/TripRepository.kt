package com.teamtripdraw.android.domain.repository

import com.teamtripdraw.android.domain.model.point.Route

interface TripRepository {
    suspend fun startTrip(): Result<Unit>
    fun getCurrentTripId(): Long
    suspend fun getCurrentTripRoute(tripId: Long): Result<Route>
}

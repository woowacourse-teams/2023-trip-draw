package com.teamtripdraw.android.domain.repository

import com.teamtripdraw.android.domain.model.point.Route
import com.teamtripdraw.android.domain.model.trip.PreSetTripTitle
import com.teamtripdraw.android.domain.model.trip.PreviewTrip
import com.teamtripdraw.android.domain.model.trip.Trip
import com.teamtripdraw.android.domain.model.trip.TripOfAll

interface TripRepository {
    suspend fun startTrip(): Result<Unit>
    fun getCurrentTripId(): Long
    fun deleteCurrentTripId()
    suspend fun getCurrentTripRoute(tripId: Long): Result<Route>
    suspend fun getTrip(tripId: Long): Result<Trip>
    suspend fun setTripTitle(tripId: Long, preSetTripTitle: PreSetTripTitle): Result<Unit>
    suspend fun getMyTrips(): Result<List<PreviewTrip>>
    suspend fun getAllTrips(
        address: String = "",
        ageRanges: List<Int> = listOf(),
        daysOfWeek: List<Int> = listOf(),
        genders: List<Int> = listOf(),
        months: List<Int> = listOf(),
        years: List<Int> = listOf(),
        lastViewedId: Long? = null,
        limit: Int = 20,
    ): Result<List<TripOfAll>>

    suspend fun deleteTrip(tripId: Long): Result<Unit>
}

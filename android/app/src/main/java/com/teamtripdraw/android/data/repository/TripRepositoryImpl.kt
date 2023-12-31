package com.teamtripdraw.android.data.repository

import com.teamtripdraw.android.data.dataSource.trip.TripDataSource
import com.teamtripdraw.android.data.model.mapper.toData
import com.teamtripdraw.android.data.model.mapper.toDomain
import com.teamtripdraw.android.data.model.mapper.toDomainRoute
import com.teamtripdraw.android.domain.model.point.Route
import com.teamtripdraw.android.domain.model.trip.PreSetTripTitle
import com.teamtripdraw.android.domain.model.trip.PreviewTrip
import com.teamtripdraw.android.domain.model.trip.Trip
import com.teamtripdraw.android.domain.model.trip.TripOfAll
import com.teamtripdraw.android.domain.repository.TripRepository
import javax.inject.Inject

class TripRepositoryImpl @Inject constructor(
    private val remoteTripDataSource: TripDataSource.Remote,
    private val localTripDataSource: TripDataSource.Local,
) :
    TripRepository {
    override suspend fun startTrip(): Result<Unit> =
        remoteTripDataSource.startTrip().onSuccess { tripId ->
            localTripDataSource.setCurrentTripId(tripId)
        }.map { }

    override fun getCurrentTripId(): Long =
        localTripDataSource.getCurrentTripId()

    override fun deleteCurrentTripId() {
        localTripDataSource.deleteCurrentTripId()
    }

    override suspend fun getCurrentTripRoute(tripId: Long): Result<Route> =
        remoteTripDataSource.getTripInfo(tripId).map { dataTrip ->
            dataTrip.toDomainRoute()
        }

    override suspend fun getTrip(tripId: Long): Result<Trip> =
        remoteTripDataSource.getTripInfo(tripId).map { it.toDomain() }

    override suspend fun setTripTitle(
        tripId: Long,
        preSetTripTitle: PreSetTripTitle,
    ): Result<Unit> =
        remoteTripDataSource.setTripTitle(tripId, preSetTripTitle.toData())

    override suspend fun getMyTrips(): Result<List<PreviewTrip>> =
        remoteTripDataSource.getMyTrips().map { trips -> trips.map { it.toDomain() } }

    override suspend fun getAllTrips(
        address: String,
        ageRanges: List<Int>,
        daysOfWeek: List<Int>,
        genders: List<Int>,
        months: List<Int>,
        years: List<Int>,
        lastViewedId: Long?,
        limit: Int,
    ): Result<List<TripOfAll>> =
        remoteTripDataSource.getAllTrips(
            address = address,
            ageRanges = ageRanges,
            daysOfWeek = daysOfWeek,
            genders = genders,
            months = months,
            years = years,
            lastViewedId = lastViewedId,
            limit = limit,
        ).map { trip ->
            trip.map { it.toDomain() }
        }

    override suspend fun deleteTrip(tripId: Long): Result<Unit> =
        remoteTripDataSource.deleteTrip(tripId)
}

package com.teamtripdraw.android.data.repository

import com.teamtripdraw.android.data.dataSource.createTrip.TripDataSource
import com.teamtripdraw.android.domain.repository.TripRepository

class TripRepositoryImpl(
    private val remoteTripDataSource: TripDataSource.Remote,
    private val localTripDataSource: TripDataSource.Local
) :
    TripRepository {
    override suspend fun startTrip(): Result<Unit> =
        remoteTripDataSource.startTrip().process { body, headers ->
            localTripDataSource.setTripId(body.tripId)
            Result.success(Unit)
        }
}

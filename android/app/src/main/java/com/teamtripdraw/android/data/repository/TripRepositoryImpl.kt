package com.teamtripdraw.android.data.repository

import com.teamtripdraw.android.data.dataSource.createTrip.TripDataSource
import com.teamtripdraw.android.domain.repository.TripRepository

class TripRepositoryImpl(private val remoteTripDataSource: TripDataSource.Remote) :
    TripRepository {
    override suspend fun startTrip(): Result<Long> =
        remoteTripDataSource.startTrip().process { body, headers ->
            Result.success(body.tripId)
        }
}

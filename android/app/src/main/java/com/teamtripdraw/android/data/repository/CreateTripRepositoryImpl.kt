package com.teamtripdraw.android.data.repository

import com.teamtripdraw.android.data.dataSource.createTrip.CreateTripDataSource
import com.teamtripdraw.android.domain.repository.CreateTripRepository

class CreateTripRepositoryImpl(private val remoteCreateTripDataSource: CreateTripDataSource.Remote) :
    CreateTripRepository {
    override suspend fun startTrip(): Result<Long> =
        remoteCreateTripDataSource.startTrip().process { body, headers ->
            Result.success(body.tripId)
        }
}

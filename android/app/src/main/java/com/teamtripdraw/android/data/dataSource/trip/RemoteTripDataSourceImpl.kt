package com.teamtripdraw.android.data.dataSource.trip

import com.teamtripdraw.android.data.httpClient.service.CreateTripService

class RemoteTripDataSourceImpl(private val createTripService: CreateTripService) :
    TripDataSource.Remote {
    override suspend fun startTrip(): Result<Long> =
        createTripService.startTrip().process { body, headers ->
            Result.success(body.tripId)
        }
}

package com.teamtripdraw.android.data.dataSource.trip

import com.teamtripdraw.android.data.httpClient.dto.mapper.toData
import com.teamtripdraw.android.data.httpClient.service.CreateTripService
import com.teamtripdraw.android.data.httpClient.service.GetTripInfoService
import com.teamtripdraw.android.data.model.DataTrip

class RemoteTripDataSourceImpl(
    private val createTripService: CreateTripService,
    private val getTripInfoService: GetTripInfoService
) :
    TripDataSource.Remote {
    override suspend fun startTrip(): Result<Long> =
        createTripService.startTrip().process { body, _ ->
            Result.success(body.tripId)
        }

    override suspend fun getTripInfo(tripId: Long): Result<DataTrip> =
        getTripInfoService.getTripInfo(tripId).process { body, _ ->
            Result.success(body)
        }.map { it.toData() }
}

package com.teamtripdraw.android.data.dataSource.trip

import com.teamtripdraw.android.data.httpClient.dto.mapper.toData
import com.teamtripdraw.android.data.httpClient.dto.request.SetTripTitleRequest
import com.teamtripdraw.android.data.httpClient.service.CreateTripService
import com.teamtripdraw.android.data.httpClient.service.GetTripInfoService
import com.teamtripdraw.android.data.httpClient.service.SetTripTitleService
import com.teamtripdraw.android.data.model.DataTrip
import com.teamtripdraw.android.domain.model.trip.TripStatus

class RemoteTripDataSourceImpl(
    private val createTripService: CreateTripService,
    private val getTripInfoService: GetTripInfoService,
    private val setTripTitleService: SetTripTitleService
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

    override suspend fun setTripTitle(tripId: Long, name: String, status: TripStatus): Result<Unit> {
        val request = SetTripTitleRequest(name, status.name)
        return setTripTitleService.setTripTitle(tripId, request).process { body, headers ->
            Result.success(body)
        }
    }
}

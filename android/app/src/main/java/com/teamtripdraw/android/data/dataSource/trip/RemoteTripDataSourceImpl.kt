package com.teamtripdraw.android.data.dataSource.trip

import com.teamtripdraw.android.data.httpClient.dto.response.CreateTripResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.ResponseState
import com.teamtripdraw.android.data.httpClient.service.CreateTripService

class RemoteTripDataSourceImpl(private val createTripService: CreateTripService) :
    TripDataSource.Remote {
    override suspend fun startTrip(): ResponseState<CreateTripResponse> =
        createTripService.startTrip()
}

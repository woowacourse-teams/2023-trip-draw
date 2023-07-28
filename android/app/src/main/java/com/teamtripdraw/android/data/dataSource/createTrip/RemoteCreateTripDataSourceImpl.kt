package com.teamtripdraw.android.data.dataSource.createTrip

import com.teamtripdraw.android.data.httpClient.dto.response.CreateTripResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.ResponseState
import com.teamtripdraw.android.data.httpClient.service.CreateTripService

class RemoteCreateTripDataSourceImpl(private val createTripService: CreateTripService) :
    CreateTripDataSource.Remote {
    override suspend fun startTrip(): ResponseState<CreateTripResponse> =
        createTripService.startTrip()
}

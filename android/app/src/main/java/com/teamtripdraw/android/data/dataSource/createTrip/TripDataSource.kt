package com.teamtripdraw.android.data.dataSource.createTrip

import com.teamtripdraw.android.data.httpClient.dto.response.CreateTripResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.ResponseState

interface TripDataSource {
    interface Local
    interface Remote {
        suspend fun startTrip(): ResponseState<CreateTripResponse>
    }
}

package com.teamtripdraw.android.data.dataSource.trip

import com.teamtripdraw.android.data.httpClient.dto.response.CreateTripResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.ResponseState

interface TripDataSource {
    interface Local {
        fun setCurrentTripId(tripId: Long)

        fun getCurrentTripId(): Long

        fun deleteCurrentTripId()
    }

    interface Remote {
        suspend fun startTrip(): ResponseState<CreateTripResponse>
    }
}

package com.teamtripdraw.android.data.httpClient.service

import com.teamtripdraw.android.data.httpClient.dto.response.CreateTripResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState.ResponseState
import retrofit2.http.POST

interface CreateTripService {
    @POST("/trips")
    suspend fun startTrip(): ResponseState<CreateTripResponse>
}

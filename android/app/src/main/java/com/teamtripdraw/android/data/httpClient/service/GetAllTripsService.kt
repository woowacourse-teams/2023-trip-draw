package com.teamtripdraw.android.data.httpClient.service

import com.teamtripdraw.android.data.httpClient.dto.response.GetAllTripsResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState.ResponseState
import retrofit2.http.GET

interface GetAllTripsService {
    @GET("/trips")
    suspend fun getAllTrips(): ResponseState<GetAllTripsResponse>
}

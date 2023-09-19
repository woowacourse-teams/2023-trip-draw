package com.teamtripdraw.android.data.httpClient.service

import com.teamtripdraw.android.data.httpClient.dto.request.GetAllTripsRequest
import com.teamtripdraw.android.data.httpClient.dto.response.GetAllTripsResponse
import com.teamtripdraw.android.data.httpClient.dto.response.GetMyTripsResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState.ResponseState
import retrofit2.http.Body
import retrofit2.http.GET

interface GetAllTripsService {
    @GET("/trips/me")
    suspend fun getMyTrips(): ResponseState<GetMyTripsResponse>

    @GET("/trips")
    suspend fun getAllTrips(@Body body: GetAllTripsRequest): ResponseState<GetAllTripsResponse>
}

package com.teamtripdraw.android.data.httpClient.service

import com.teamtripdraw.android.data.httpClient.dto.response.GetAllTripsResponse
import com.teamtripdraw.android.data.httpClient.dto.response.GetMyTripsResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState.ResponseState
import retrofit2.http.GET
import retrofit2.http.Query

interface GetAllTripsService {
    @GET("/trips/me")
    suspend fun getMyTrips(): ResponseState<GetMyTripsResponse>

    @GET("/trips")
    suspend fun getAllTrips(
        @Query("years") years: List<Int>,
        @Query("months") months: List<Int>,
        @Query("daysOfWeek") daysOfWeek: List<Int>,
        @Query("ageRanges") ageRanges: List<Int>,
        @Query("genders") genders: List<Int>,
        @Query("address") address: String,
        @Query("lastViewedId") lastViewedId: Long?,
        @Query("limit") limit: Int,
    ): ResponseState<GetAllTripsResponse>
}

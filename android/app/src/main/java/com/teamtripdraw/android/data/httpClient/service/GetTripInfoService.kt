package com.teamtripdraw.android.data.httpClient.service

import com.teamtripdraw.android.data.httpClient.dto.response.GetTripInfoResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.ResponseState
import retrofit2.http.GET
import retrofit2.http.Path

interface GetTripInfoService {
    @GET("/trips/{tripId}")
    suspend fun getTripInfo(
        @Path("tripId") tripId: Long,
    ): ResponseState<GetTripInfoResponse>
}

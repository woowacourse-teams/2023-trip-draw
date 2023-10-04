package com.teamtripdraw.android.data.httpClient.service

import com.teamtripdraw.android.data.httpClient.dto.response.GetPointResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState.ResponseState
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GetPointService {
    @GET("/points/{pointId}")
    suspend fun getPoint(
        @Path("pointId") pointId: Long,
        @Query("tripId") tripId: Long,
    ): ResponseState<GetPointResponse>
}

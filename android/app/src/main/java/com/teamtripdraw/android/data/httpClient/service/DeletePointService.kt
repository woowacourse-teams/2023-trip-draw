package com.teamtripdraw.android.data.httpClient.service

import com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState.ResponseState
import retrofit2.http.DELETE
import retrofit2.http.Path
import retrofit2.http.Query

interface DeletePointService {
    @DELETE("/points/{pointId}")
    suspend fun deletePoint(
        @Path("pointId") pointId: Long,
        @Query("tripId") tripId: Long,
    ): ResponseState<Unit>
}

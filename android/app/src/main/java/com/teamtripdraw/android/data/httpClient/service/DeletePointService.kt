package com.teamtripdraw.android.data.httpClient.service

import com.teamtripdraw.android.data.httpClient.dto.request.DeletePointRequest
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.ResponseState
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Path

interface DeletePointService {
    @DELETE("/points/{pointId}")
    suspend fun deletePoint(
        @Path("pointId") pointId: Long,
        @Body body: DeletePointRequest
    ): ResponseState<Unit>
}

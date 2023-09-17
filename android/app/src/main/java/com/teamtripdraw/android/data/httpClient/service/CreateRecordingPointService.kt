package com.teamtripdraw.android.data.httpClient.service

import com.teamtripdraw.android.data.httpClient.dto.request.CreateRecordingPointRequest
import com.teamtripdraw.android.data.httpClient.dto.response.CreateRecordingPointResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.ResponseState
import retrofit2.http.Body
import retrofit2.http.POST

interface CreateRecordingPointService {
    @POST("/points")
    suspend fun createRecordingPoint(
        @Body body: CreateRecordingPointRequest,
    ): ResponseState<CreateRecordingPointResponse>
}

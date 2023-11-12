package com.teamtripdraw.android.data.httpClient.service

import com.teamtripdraw.android.data.httpClient.dto.response.GetPointPostResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState.ResponseState
import retrofit2.http.GET
import retrofit2.http.Path

interface GetPointPostService {
    @GET("/points/{pointId}/post")
    suspend fun getPost(
        @Path("pointId") pointId: Long,
    ): ResponseState<GetPointPostResponse>
}

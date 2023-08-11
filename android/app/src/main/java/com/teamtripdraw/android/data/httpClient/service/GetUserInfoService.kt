package com.teamtripdraw.android.data.httpClient.service

import com.teamtripdraw.android.data.httpClient.dto.response.GetUserInfoResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.ResponseState
import retrofit2.http.GET
import retrofit2.http.Query

interface GetUserInfoService {
    @GET("/members/{memberId}")
    suspend fun getUserInfo(
        @Query("accessToken") accessToken: String,
    ): ResponseState<GetUserInfoResponse>
}

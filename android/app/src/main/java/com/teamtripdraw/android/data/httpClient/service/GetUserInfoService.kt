package com.teamtripdraw.android.data.httpClient.service

import com.teamtripdraw.android.data.httpClient.dto.response.GetUserInfoResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState.ResponseState
import retrofit2.http.GET

interface GetUserInfoService {
    @GET("/members/me")
    suspend fun getUserInfo(): ResponseState<GetUserInfoResponse>
}

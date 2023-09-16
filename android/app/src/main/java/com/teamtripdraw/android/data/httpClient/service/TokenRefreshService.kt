package com.teamtripdraw.android.data.httpClient.service

import com.teamtripdraw.android.data.httpClient.dto.request.TokenRefreshRequest
import com.teamtripdraw.android.data.httpClient.dto.response.TokenRefreshResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState.ResponseState
import retrofit2.http.Body
import retrofit2.http.POST

interface TokenRefreshService {
    @POST("oauth/refresh")
    suspend fun tokenRefresh(
        @Body body: TokenRefreshRequest,
    ): ResponseState<TokenRefreshResponse>
}

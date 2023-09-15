package com.teamtripdraw.android.data.httpClient.service

import com.teamtripdraw.android.data.httpClient.dto.request.LoginRequest
import com.teamtripdraw.android.data.httpClient.dto.response.LoginResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState.ResponseState
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("/oauth/login")
    suspend fun login(
        @Body body: LoginRequest,
    ): ResponseState<LoginResponse>
}

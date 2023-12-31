package com.teamtripdraw.android.data.httpClient.service

import com.teamtripdraw.android.data.httpClient.dto.request.NicknameSetUpRequest
import com.teamtripdraw.android.data.httpClient.dto.response.NicknameSetupResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState.ResponseState
import retrofit2.http.Body
import retrofit2.http.POST

interface NicknameSetupService {
    @POST("/oauth/register")
    suspend fun setNickname(
        @Body body: NicknameSetUpRequest,
    ): ResponseState<NicknameSetupResponse>
}

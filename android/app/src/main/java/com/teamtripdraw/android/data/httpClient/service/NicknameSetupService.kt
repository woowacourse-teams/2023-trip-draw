package com.teamtripdraw.android.data.httpClient.service

import com.teamtripdraw.android.data.httpClient.dto.request.NicknameSetUpRequest
import com.teamtripdraw.android.data.httpClient.dto.response.NicknameSetupResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.ResponseState
import retrofit2.http.Body
import retrofit2.http.POST

interface NicknameSetupService {
    @POST("/members")
    suspend fun setNickName(
        @Body body: NicknameSetUpRequest
    ): ResponseState<NicknameSetupResponse>
}

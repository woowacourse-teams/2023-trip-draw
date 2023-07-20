package com.teamtripdraw.android.data.httpClient.service

import com.teamtripdraw.android.data.httpClient.dto.request.SetNickNameRequest
import com.teamtripdraw.android.data.httpClient.dto.response.SetNickNameResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.ResponseState
import retrofit2.http.Body
import retrofit2.http.POST

interface SetNickNameService {
    @POST("/members")
    suspend fun setNickName(
        @Body body: SetNickNameRequest
    ): ResponseState<SetNickNameResponse>
}

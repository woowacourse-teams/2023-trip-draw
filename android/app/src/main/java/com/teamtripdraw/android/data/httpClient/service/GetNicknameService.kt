package com.teamtripdraw.android.data.httpClient.service

import com.teamtripdraw.android.data.httpClient.dto.response.GetNicknameResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.ResponseState
import retrofit2.http.GET
import retrofit2.http.Path

interface GetNicknameService {
    @GET("/members/{memberId}")
    suspend fun getNickname(
        @Path("memberId") memberId: Long
    ): ResponseState<GetNicknameResponse>
}

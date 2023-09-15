package com.teamtripdraw.android.data.httpClient.service

import com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState.ResponseState
import retrofit2.http.DELETE
import retrofit2.http.Query

interface UnsubscribeService {
    @DELETE("/members")
    suspend fun unsubscribe(
        @Query("code") accessToken: String,
    ): ResponseState<Unit>
}

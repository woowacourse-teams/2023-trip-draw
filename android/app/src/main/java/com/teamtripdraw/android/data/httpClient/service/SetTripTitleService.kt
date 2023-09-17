package com.teamtripdraw.android.data.httpClient.service

import com.teamtripdraw.android.data.httpClient.dto.request.SetTripTitleRequest
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.ResponseState
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.Path

interface SetTripTitleService {

    @PATCH("/trips/{tripId}")
    suspend fun setTripTitle(
        @Path("tripId") tripId: Long,
        @Body body: SetTripTitleRequest,
    ): ResponseState<Unit>
}

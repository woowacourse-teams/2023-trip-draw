package com.teamtripdraw.android.data.httpClient.service

import com.teamtripdraw.android.data.httpClient.dto.response.GetAddressesResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState.ResponseState
import retrofit2.http.GET
import retrofit2.http.Query

interface GetAddressesService {
    @GET("/areas")
    suspend fun getAddresses(
        @Query("sido") siDo: String,
        @Query("sigungu") siGunGu: String,
    ): ResponseState<GetAddressesResponse>
}

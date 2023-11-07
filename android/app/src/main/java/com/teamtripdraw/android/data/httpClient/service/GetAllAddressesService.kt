package com.teamtripdraw.android.data.httpClient.service

import com.teamtripdraw.android.data.httpClient.dto.response.GetAllAddressesResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState.ResponseState
import retrofit2.http.GET

interface GetAllAddressesService {
    @GET("/areas/all")
    suspend fun getAddresses(): ResponseState<GetAllAddressesResponse>
}

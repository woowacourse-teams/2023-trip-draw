package com.teamtripdraw.android.data.httpClient.service

import com.teamtripdraw.android.data.httpClient.dto.response.GetReverseGeocodingResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState.ResponseState
import retrofit2.http.GET
import retrofit2.http.Query

interface NaverGeocodingService {
    @GET("v2/gc")
    suspend fun getReverseGeocoding(
        @Query("request") request: String = "coordsToaddr",
        @Query("coords") coords: String,
        @Query("sourcecrs") sourcecrs: String = "epsg:4326",
        @Query("output") output: String = "json",
        @Query("orders") orders: String = "legalcode,admcode",
    ): ResponseState<GetReverseGeocodingResponse>
}

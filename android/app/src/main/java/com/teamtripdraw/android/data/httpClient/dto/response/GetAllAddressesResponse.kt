package com.teamtripdraw.android.data.httpClient.dto.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetAllAddressesResponse(
    val areas: List<AllAddressResponse>
)

@JsonClass(generateAdapter = true)
data class AllAddressResponse(
    @Json(name = "sido")
    val sido: String,
    @Json(name = "sigungu")
    val sigungu: String,
    @Json(name = "eupmyeondong")
    val eupmyeondong: String,
)

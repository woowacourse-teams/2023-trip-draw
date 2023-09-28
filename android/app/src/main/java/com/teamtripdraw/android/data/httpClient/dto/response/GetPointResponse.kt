package com.teamtripdraw.android.data.httpClient.dto.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetPointResponse(
    @Json(name = "pointId")
    val pointId: Long,
    @Json(name = "latitude")
    val latitude: Double,
    @Json(name = "longitude")
    val longitude: Double,
    @Json(name = "hasPost")
    val hasPost: Boolean,
    @Json(name = "recordedAt")
    val recordedAt: String,
)

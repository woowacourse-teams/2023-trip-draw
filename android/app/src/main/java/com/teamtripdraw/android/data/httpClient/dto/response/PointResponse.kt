package com.teamtripdraw.android.data.httpClient.dto.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PointResponse(
    @Json(name = "latitude")
    val latitude: Double,
    @Json(name = "longitude")
    val longitude: Double,
    @Json(name = "pointId")
    val pointId: Long,
    @Json(name = "recordedAt")
    val recordedAt: String
)


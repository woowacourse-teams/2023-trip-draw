package com.teamtripdraw.android.data.httpClient.dto.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateRecordingPointRequest(
    @Json(name = "tripId")
    val tripId: Long,
    @Json(name = "latitude")
    val latitude: Double,
    @Json(name = "longitude")
    val longitude: Double,
    @Json(name = "recordedAt")
    val recordedAt: String,
)

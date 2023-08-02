package com.teamtripdraw.android.data.httpClient.dto.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddCurrentPointPostRequest(
    @Json(name = "tripId")
    val tripId: Long,
    @Json(name = "title")
    val title: String,
    @Json(name = "address")
    val address: String,
    @Json(name = "writing")
    val writing: String,
    @Json(name = "latitude")
    val latitude: Double,
    @Json(name = "longitude")
    val longitude: Double,
    @Json(name = "recordedAt")
    val recordedAt: String
)
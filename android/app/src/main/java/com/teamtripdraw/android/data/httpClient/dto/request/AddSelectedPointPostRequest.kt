package com.teamtripdraw.android.data.httpClient.dto.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddSelectedPointPostRequest(
    @Json(name = "tripId")
    val tripId: Long,
    @Json(name = "pointId")
    val pointId: Long,
    @Json(name = "title")
    val title: String,
    @Json(name = "address")
    val address: String,
    @Json(name = "writing")
    val writing: String
)

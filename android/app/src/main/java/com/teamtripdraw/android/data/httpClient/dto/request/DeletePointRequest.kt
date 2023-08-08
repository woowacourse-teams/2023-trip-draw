package com.teamtripdraw.android.data.httpClient.dto.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DeletePointRequest(
    @Json(name = "tripId")
    val tripId: Long
)

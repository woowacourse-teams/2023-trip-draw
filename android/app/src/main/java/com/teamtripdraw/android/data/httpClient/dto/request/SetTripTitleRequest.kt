package com.teamtripdraw.android.data.httpClient.dto.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SetTripTitleRequest(
    @Json(name = "name")
    val name: String,
    @Json(name = "status")
    val status: String
)

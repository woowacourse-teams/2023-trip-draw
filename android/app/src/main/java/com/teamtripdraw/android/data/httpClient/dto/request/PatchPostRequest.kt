package com.teamtripdraw.android.data.httpClient.dto.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PatchPostRequest(
    @Json(name = "title")
    val title: String,
    @Json(name = "writing")
    val writing: String,
)

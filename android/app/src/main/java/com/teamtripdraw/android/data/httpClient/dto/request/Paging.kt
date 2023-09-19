package com.teamtripdraw.android.data.httpClient.dto.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Paging(
    @Json(name = "lastViewedId")
    val lastViewedId: Long?,
    @Json(name = "limit")
    val limit: Int,
)

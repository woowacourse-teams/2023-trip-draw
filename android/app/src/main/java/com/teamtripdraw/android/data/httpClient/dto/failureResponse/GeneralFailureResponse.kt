package com.teamtripdraw.android.data.httpClient.dto.failureResponse

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeneralFailureResponse(
    @Json(name = "exceptionCode")
    val exceptionCode: String,
    @Json(name = "message")
    val message: String,
)

package com.teamtripdraw.android.data.httpClient.dto.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequest(
    @Json(name = "oauthType")
    val oauthType: String,
    @Json(name = "oauthToken")
    val oauthToken: String,
)

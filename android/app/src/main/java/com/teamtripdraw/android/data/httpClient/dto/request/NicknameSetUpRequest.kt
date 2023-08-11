package com.teamtripdraw.android.data.httpClient.dto.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NicknameSetUpRequest(
    @Json(name = "nickname")
    val nickname: String,
    @Json(name = "oAuthToken")
    val oAuthToken: String,
    @Json(name = "oAuthType")
    val oAuthType: String
)

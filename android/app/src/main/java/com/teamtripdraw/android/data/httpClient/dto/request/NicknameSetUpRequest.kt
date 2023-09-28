package com.teamtripdraw.android.data.httpClient.dto.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NicknameSetUpRequest(
    @Json(name = "nickname")
    val nickname: String,
    @Json(name = "oauthToken")
    val oauthToken: String,
    @Json(name = "oauthType")
    val oauthType: String,
)

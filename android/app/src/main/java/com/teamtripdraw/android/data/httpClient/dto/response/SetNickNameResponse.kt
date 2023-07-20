package com.teamtripdraw.android.data.httpClient.dto.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SetNickNameResponse(
    @Json(name = "memberId")
    val memberId: String,
    @Json(name = "nickname")
    val nickname: String
)

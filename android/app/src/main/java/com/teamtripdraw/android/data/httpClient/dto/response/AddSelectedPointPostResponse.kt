package com.teamtripdraw.android.data.httpClient.dto.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddSelectedPointPostResponse(
    @Json(name = "postId")
    val postId: Long
)

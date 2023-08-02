package com.teamtripdraw.android.data.httpClient.dto.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetPostResponse(
    @Json(name = "address")
    val address: String,
    @Json(name = "point")
    val point: GetPostPointResponse,
    @Json(name = "postId")
    val postId: Long,
    @Json(name = "postImageUrl")
    val postImageUrl: String?,
    @Json(name = "routeImageUrl")
    val routeImageUrl: String?,
    @Json(name = "title")
    val title: String,
    @Json(name = "tripId")
    val tripId: Long,
    @Json(name = "writing")
    val writing: String
)

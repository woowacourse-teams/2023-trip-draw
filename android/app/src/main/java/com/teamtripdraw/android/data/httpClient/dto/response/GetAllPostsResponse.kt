package com.teamtripdraw.android.data.httpClient.dto.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetAllPostsResponse(
    @Json(name = "hasNextPage")
    val hasNextPage: Boolean,
    @Json(name = "posts")
    val posts: List<Post>,
)

@JsonClass(generateAdapter = true)
data class Post(
    @Json(name = "address")
    val address: String,
    @Json(name = "postId")
    val postId: Long,
    @Json(name = "postImageUrl")
    val postImageUrl: String,
    @Json(name = "recordedAt")
    val recordedAt: String,
    @Json(name = "routeImageUrl")
    val routeImageUrl: String,
    @Json(name = "title")
    val title: String,
    @Json(name = "tripId")
    val tripId: Long,
    @Json(name = "writing")
    val writing: String,
    @Json(name = "memberNickname")
    val memberNickname: String,
)

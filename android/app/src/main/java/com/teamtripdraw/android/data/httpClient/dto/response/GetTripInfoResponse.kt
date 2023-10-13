package com.teamtripdraw.android.data.httpClient.dto.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetTripInfoResponse(
    @Json(name = "tripId")
    val tripId: Long,
    @Json(name = "name")
    val name: String,
    @Json(name = "route")
    val route: List<GetTripInfoPoint>,
    @Json(name = "status")
    val status: String,
    @Json(name = "imageUrl")
    val imageUrl: String,
    @Json(name = "routeImageUrl")
    val routeImageUrl: String,
    @Json(name = "isMine")
    val isMine: Boolean,
    @Json(name = "authorNickname")
    val authorNickname: String,
)

@JsonClass(generateAdapter = true)
data class GetTripInfoPoint(
    @Json(name = "pointId")
    val pointId: Long,
    @Json(name = "latitude")
    val latitude: Double,
    @Json(name = "longitude")
    val longitude: Double,
    @Json(name = "hasPost")
    val hasPost: Boolean,
    @Json(name = "recordedAt")
    val recordedAt: String,
)

package com.teamtripdraw.android.data.httpClient.dto.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetAllTripsResponse(
    @Json(name = "hasNextPage")
    val hasNextPage: Boolean,
    @Json(name = "trips")
    val trips: List<Trip>,
)

@JsonClass(generateAdapter = true)
data class Trip(
    @Json(name = "endTime")
    val endTime: String,
    @Json(name = "imageUrl")
    val imageUrl: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "routeImageUrl")
    val routeImageUrl: String,
    @Json(name = "startTime")
    val startTime: String,
    @Json(name = "tripId")
    val tripId: Long,
    @Json(name = "memberNickname")
    val memberNickname: String,
)

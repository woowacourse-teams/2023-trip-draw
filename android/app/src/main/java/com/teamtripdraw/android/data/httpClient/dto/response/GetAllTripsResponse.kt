package com.teamtripdraw.android.data.httpClient.dto.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetAllTripsResponse(
    @Json(name = "trips")
    val getPreviewTripResponses: List<GetPreviewTripResponse>,
)

@JsonClass(generateAdapter = true)
data class GetPreviewTripResponse(
    @Json(name = "imageUrl")
    val imageUrl: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "routeImageUrl")
    val routeImageUrl: String,
    @Json(name = "tripId")
    val tripId: Long,
)

package com.teamtripdraw.android.data.httpClient.dto.mapper

import com.teamtripdraw.android.data.httpClient.dto.request.SetTripTitleRequest
import com.teamtripdraw.android.data.httpClient.dto.response.GetMyTripsResponse
import com.teamtripdraw.android.data.httpClient.dto.response.GetPreviewTripResponse
import com.teamtripdraw.android.data.httpClient.dto.response.GetTripInfoPoint
import com.teamtripdraw.android.data.httpClient.dto.response.GetTripInfoResponse
import com.teamtripdraw.android.data.model.DataPoint
import com.teamtripdraw.android.data.model.DataPreSetTripTitle
import com.teamtripdraw.android.data.model.DataPreviewTrip
import com.teamtripdraw.android.data.model.DataTrip

fun GetTripInfoResponse.toData(): DataTrip =
    DataTrip(
        tripId = tripId,
        name = name,
        route = route.map { it.toData() },
        status = status,
        imageUrl = imageUrl,
        routeImageUrl = routeImageUrl,
    )

fun GetTripInfoPoint.toData(): DataPoint =
    DataPoint(
        pointId = pointId,
        latitude = latitude,
        longitude = longitude,
        hasPost = hasPost,
        recordedAt = recordedAt,
    )

fun DataPreSetTripTitle.toHttpRequest(): SetTripTitleRequest =
    SetTripTitleRequest(name = name, status = status.name)

fun GetMyTripsResponse.toData(): List<DataPreviewTrip> =
    getPreviewTripResponses.map { it.toData() }

fun GetPreviewTripResponse.toData(): DataPreviewTrip =
    DataPreviewTrip(
        id = tripId,
        name = this.name,
        imageUrl = imageUrl,
        routeImageUrl = routeImageUrl,
    )

package com.teamtripdraw.android.data.httpClient.dto.mapper

import com.teamtripdraw.android.data.httpClient.dto.request.SetTripTitleRequest
import com.teamtripdraw.android.data.httpClient.dto.response.GetAllTripsResponse
import com.teamtripdraw.android.data.httpClient.dto.response.GetMyTripsResponse
import com.teamtripdraw.android.data.httpClient.dto.response.GetPreviewTripResponse
import com.teamtripdraw.android.data.httpClient.dto.response.GetTripInfoPoint
import com.teamtripdraw.android.data.httpClient.dto.response.GetTripInfoResponse
import com.teamtripdraw.android.data.model.DataPoint
import com.teamtripdraw.android.data.model.DataPreSetTripTitle
import com.teamtripdraw.android.data.model.DataPreviewTrip
import com.teamtripdraw.android.data.model.DataTrip
import com.teamtripdraw.android.data.model.DataTripOfAll

fun GetTripInfoResponse.toData(): DataTrip =
    DataTrip(
        tripId = tripId,
        name = name,
        route = route.map { it.toData() },
        status = status,
        imageUrl = imageUrl,
        routeImageUrl = routeImageUrl,
        isMine = isMine,
        authorNickname = authorNickname,
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

fun GetAllTripsResponse.toData(): List<DataTripOfAll> {
    return this.trips.map {
        DataTripOfAll(
            tripId = it.tripId,
            name = it.name,
            imageUrl = it.imageUrl,
            routeImageUrl = it.routeImageUrl,
            startTime = it.startTime,
            endTime = it.endTime,
        )
    }
}

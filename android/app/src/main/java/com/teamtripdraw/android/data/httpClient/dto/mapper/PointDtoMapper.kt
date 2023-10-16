package com.teamtripdraw.android.data.httpClient.dto.mapper

import com.teamtripdraw.android.data.httpClient.dto.request.CreateRecordingPointRequest
import com.teamtripdraw.android.data.httpClient.dto.response.GetPointPostResponse
import com.teamtripdraw.android.data.model.DataPost
import com.teamtripdraw.android.data.model.DataPrePoint

fun DataPrePoint.toHttpRequest(tripId: Long): CreateRecordingPointRequest =
    CreateRecordingPointRequest(
        tripId = tripId,
        latitude = latitude,
        longitude = longitude,
        recordedAt = recordedAt,
    )

fun GetPointPostResponse.toData(): DataPost {
    return DataPost(
        postId = postId,
        tripId = tripId,
        title = title,
        writing = writing,
        address = address,
        point = point.toData(),
        postImageUrl = postImageUrl,
        routeImageUrl = routeImageUrl,
    )
}
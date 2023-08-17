package com.teamtripdraw.android.data.httpClient.dto.mapper

import com.teamtripdraw.android.data.httpClient.dto.request.AddPostRequest
import com.teamtripdraw.android.data.httpClient.dto.request.PatchPostRequest
import com.teamtripdraw.android.data.httpClient.dto.response.GetPointResponse
import com.teamtripdraw.android.data.model.DataPoint
import com.teamtripdraw.android.data.model.DataPrePatchPost
import com.teamtripdraw.android.data.model.DataPrePost

fun DataPrePost.toHttpRequest(): AddPostRequest {
    return AddPostRequest(
        tripId = tripId,
        pointId = pointId,
        title = title,
        writing = writing,
        address = address,
    )
}

fun DataPrePatchPost.toHttpRequest(): PatchPostRequest {
    return PatchPostRequest(
        title = title,
        writing = writing,
    )
}

fun GetPointResponse.toData(): DataPoint {
    return DataPoint(
        pointId = pointId,
        latitude = latitude,
        longitude = longitude,
        hasPost = hasPost,
        recordedAt = recordedAt,
    )
}

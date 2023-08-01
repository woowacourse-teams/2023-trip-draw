package com.teamtripdraw.android.data.httpClient.dto.mapper

import com.teamtripdraw.android.data.httpClient.dto.request.AddCurrentPointPostRequest
import com.teamtripdraw.android.data.httpClient.dto.request.AddSelectedPointPostRequest
import com.teamtripdraw.android.data.model.DataPreCurrentPointPost
import com.teamtripdraw.android.data.model.DataPreSelectedPointPost

fun DataPreSelectedPointPost.toHttpRequest(): AddSelectedPointPostRequest {
    return AddSelectedPointPostRequest(
        tripId = tripId,
        pointId = pointId,
        title = title,
        writing = writing,
        address = address
    )
}

fun DataPreCurrentPointPost.toHttpRequest(): AddCurrentPointPostRequest {
    return AddCurrentPointPostRequest(
        tripId = tripId,
        title = title,
        writing = writing,
        address = address,
        latitude = latitude,
        longitude = longitude,
        recordedAt = recordedAt
    )
}

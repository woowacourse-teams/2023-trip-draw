package com.teamtripdraw.android.data.httpClient.dto.mapper

import com.teamtripdraw.android.data.httpClient.dto.request.AddPostRequest
import com.teamtripdraw.android.data.model.DataPrePost

fun DataPrePost.toHttpRequest(): AddPostRequest {
    return AddPostRequest(
        tripId = tripId,
        pointId = pointId,
        title = title,
        writing = writing,
        address = address
    )
}

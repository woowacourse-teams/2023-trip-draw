package com.teamtripdraw.android.data.httpClient.dto.mapper

import com.teamtripdraw.android.data.httpClient.dto.request.CreateRecordingPointRequest
import com.teamtripdraw.android.data.model.DataPrePoint

fun DataPrePoint.toHttpRequest(tripId: Long): CreateRecordingPointRequest =
    CreateRecordingPointRequest(
        tripId = tripId,
        latitude = latitude,
        longitude = longitude,
        recordedAt = recordedAt
    )

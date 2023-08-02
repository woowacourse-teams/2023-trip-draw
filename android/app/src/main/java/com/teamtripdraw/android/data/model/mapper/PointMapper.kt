package com.teamtripdraw.android.data.model.mapper

import com.teamtripdraw.android.data.model.DataPrePoint
import com.teamtripdraw.android.domain.model.point.PrePoint
import com.teamtripdraw.android.support.framework.presentation.LocalDateTimeFormatter.isoLocalDateTimeFormatter

fun PrePoint.toData(): DataPrePoint =
    DataPrePoint(
        latitude = latitude,
        longitude = longitude,
        recordedAt = recordedAt.format(isoLocalDateTimeFormatter)
    )


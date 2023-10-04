package com.teamtripdraw.android.data.model.mapper

import com.teamtripdraw.android.data.model.DataPoint
import com.teamtripdraw.android.data.model.DataPrePoint
import com.teamtripdraw.android.domain.model.point.Point
import com.teamtripdraw.android.domain.model.point.PrePoint
import com.teamtripdraw.android.support.framework.presentation.LocalDateTimeFormatter.isoRemoveNanoSecondFormatter
import java.time.LocalDateTime

fun PrePoint.toData(): DataPrePoint =
    DataPrePoint(
        latitude = latitude,
        longitude = longitude,
        recordedAt = recordedAt.format(isoRemoveNanoSecondFormatter),
    )

fun DataPoint.toDomain(): Point =
    Point(
        pointId = pointId,
        latitude = latitude,
        longitude = longitude,
        hasPost = hasPost,
        recordedAt = LocalDateTime.parse(recordedAt),
    )

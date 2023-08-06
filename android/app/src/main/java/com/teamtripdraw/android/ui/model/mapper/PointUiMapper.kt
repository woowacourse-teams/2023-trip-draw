package com.teamtripdraw.android.ui.model.mapper

import com.teamtripdraw.android.domain.model.point.Point
import com.teamtripdraw.android.ui.model.UiPoint

fun Point.toPresentation(): UiPoint =
    UiPoint(
        pointId = pointId,
        latitude = latitude,
        longitude = longitude,
        hasPost = hasPost
    )

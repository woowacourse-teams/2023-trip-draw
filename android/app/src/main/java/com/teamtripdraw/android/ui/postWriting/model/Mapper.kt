package com.teamtripdraw.android.ui.postWriting.model

import com.teamtripdraw.android.domain.model.point.Point

fun UiPoint.toDomain(): Point {
    return Point(pointId, latitude, longitude, recordedAt)
}
fun Point.toPresentation(): UiPoint {
    return UiPoint(pointId, latitude, longitude, recordedAt)
}
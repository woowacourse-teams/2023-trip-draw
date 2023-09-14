package com.teamtripdraw.android.testDouble

import com.teamtripdraw.android.domain.model.point.Point
import com.teamtripdraw.android.domain.model.point.Route

internal fun DummyRoute(
    value: List<Point> = listOf(),
): Route = Route(
    value = value,
)

internal fun DummyRoute(
    vararg value: Point = listOf<Point>().toTypedArray(),
): Route = Route(
    value = value.toList(),
)

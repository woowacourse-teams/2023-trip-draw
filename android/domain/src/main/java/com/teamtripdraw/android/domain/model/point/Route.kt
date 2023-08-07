package com.teamtripdraw.android.domain.model.point

data class Route(
    val value: List<Point>
) {
    fun checkAvailablePolyLine(): Boolean =
        value.size >= 2
}

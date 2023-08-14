package com.teamtripdraw.android.domain.model.point

data class Route(
    val value: List<Point>
) {
    fun checkAvailablePolyLine(): Boolean =
        value.size >= AVAILABLE_POLY_LINE_SIZE

    companion object {
        private const val AVAILABLE_POLY_LINE_SIZE = 2
    }
}

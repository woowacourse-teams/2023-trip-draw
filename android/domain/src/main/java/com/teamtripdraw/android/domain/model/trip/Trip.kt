package com.teamtripdraw.android.domain.model.trip

import com.teamtripdraw.android.domain.model.point.Route

data class Trip(
    val tripId: Long,
    val name: String,
    val route: Route,
    val status: String,
    val imageUrl: String?,
    val routeImageUrl: String?,
) {
    companion object {
        const val NULL_SUBSTITUTE_ID = -1L
    }
}

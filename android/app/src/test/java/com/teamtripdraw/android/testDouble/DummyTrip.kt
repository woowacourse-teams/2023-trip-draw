package com.teamtripdraw.android.testDouble

import com.teamtripdraw.android.domain.model.point.Route
import com.teamtripdraw.android.domain.model.trip.Trip

internal fun DummyTrip(
    tripId: Long = 0,
    name: String = "브레멘의 테스트 여행",
    route: Route = DummyRoute(),
    status: String = "",
    imageUrl: String = "",
    routeImageUrl: String = "",
): Trip = Trip(
    tripId = tripId,
    name = name,
    route = route,
    status = status,
    imageUrl = imageUrl,
    routeImageUrl = routeImageUrl,
)

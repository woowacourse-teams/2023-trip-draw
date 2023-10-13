package com.teamtripdraw.android.data.model.mapper

import com.teamtripdraw.android.data.model.DataPreSetTripTitle
import com.teamtripdraw.android.data.model.DataPreviewTrip
import com.teamtripdraw.android.data.model.DataTrip
import com.teamtripdraw.android.data.model.DataTripOfAll
import com.teamtripdraw.android.domain.model.point.Route
import com.teamtripdraw.android.domain.model.trip.PreSetTripTitle
import com.teamtripdraw.android.domain.model.trip.PreviewTrip
import com.teamtripdraw.android.domain.model.trip.Trip
import com.teamtripdraw.android.domain.model.trip.TripOfAll
import java.time.LocalDateTime

fun DataTrip.toDomainRoute(): Route =
    Route(route.map { it.toDomain() })

fun DataTrip.toDomain(): Trip =
    Trip(
        tripId = tripId,
        name = name,
        route = Route(route.map { it.toDomain() }),
        status = status,
        imageUrl = imageUrl,
        routeImageUrl = routeImageUrl,
        isMine = isMine,
        authorNickname = authorNickname,
    )

fun PreSetTripTitle.toData(): DataPreSetTripTitle =
    DataPreSetTripTitle(name, status)

fun DataPreviewTrip.toDomain(): PreviewTrip =
    PreviewTrip(id = id, name = name, imageUrl = imageUrl, routeImageUrl = routeImageUrl)

fun DataTripOfAll.toDomain(): TripOfAll =
    TripOfAll(
        tripId = tripId,
        name = name,
        imageUrl = imageUrl,
        routeImageUrl = routeImageUrl,
        startTime = LocalDateTime.parse(startTime),
        endTime = LocalDateTime.parse(endTime),
    )

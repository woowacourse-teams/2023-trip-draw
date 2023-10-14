package com.teamtripdraw.android.ui.model.mapper

import com.teamtripdraw.android.domain.model.point.Route
import com.teamtripdraw.android.domain.model.trip.PreviewTrip
import com.teamtripdraw.android.domain.model.trip.Trip
import com.teamtripdraw.android.domain.model.trip.TripOfAll
import com.teamtripdraw.android.support.framework.presentation.LocalDateTimeFormatter
import com.teamtripdraw.android.ui.model.UiPreviewTrip
import com.teamtripdraw.android.ui.model.UiRoute
import com.teamtripdraw.android.ui.model.UiTripOfAll
import java.time.LocalDateTime

fun Route.toPresentation(): UiRoute =
    UiRoute(
        value = value.map { it.toPresentation() },
        enablePolyLine = checkAvailablePolyLine(),
    )

fun PreviewTrip.toPresentation(): UiPreviewTrip =
    UiPreviewTrip(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl,
        routeImageUrl = this.routeImageUrl,
    )

fun Trip.toPreviewPresentation(): UiPreviewTrip =
    UiPreviewTrip(
        id = tripId,
        name = name,
        imageUrl = imageUrl,
        routeImageUrl = routeImageUrl,
    )

fun UiPreviewTrip.toDomain(): PreviewTrip =
    PreviewTrip(
        id = id,
        name = name,
        imageUrl = imageUrl,
        routeImageUrl = routeImageUrl,
    )

fun TripOfAll.toPresentation() = UiTripOfAll(
    tripId = tripId,
    name = name,
    imageUrl = imageUrl,
    routeImageUrl = routeImageUrl,
    startTime = startTime.formattedTripDate(),
    endTime = endTime.formattedTripDate(),
    isMine = isMine,
    authorNickname = authorNickname,
)

private fun LocalDateTime.formattedTripDate() =
    this.format(LocalDateTimeFormatter.displayTripDateFormatter)

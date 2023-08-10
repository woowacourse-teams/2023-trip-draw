package com.teamtripdraw.android.ui.model.mapper

import com.teamtripdraw.android.domain.model.point.Route
import com.teamtripdraw.android.domain.model.trip.PreviewTrip
import com.teamtripdraw.android.ui.model.UiPreviewTrip
import com.teamtripdraw.android.ui.model.UiRoute

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

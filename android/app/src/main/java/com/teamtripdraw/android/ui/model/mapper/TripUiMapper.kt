package com.teamtripdraw.android.ui.model.mapper

import com.teamtripdraw.android.domain.model.point.Route
import com.teamtripdraw.android.domain.model.trip.PreviewTrip
import com.teamtripdraw.android.ui.model.UiRoute
import com.teamtripdraw.android.ui.model.UiPreviewTrip

fun Route.toUi(): UiRoute =
    UiRoute(
        value = value.map { it.toUi() }
    )

fun PreviewTrip.toPresentation(): UiPreviewTrip =
    UiPreviewTrip(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl,
        routeImageUrl = this.routeImageUrl
    )

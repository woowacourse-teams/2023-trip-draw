package com.teamtripdraw.android.data.model.mapper

import com.teamtripdraw.android.data.model.DataPreSetTripTitle
import com.teamtripdraw.android.data.model.DataPreviewTrip
import com.teamtripdraw.android.data.model.DataTrip
import com.teamtripdraw.android.domain.model.point.Route
import com.teamtripdraw.android.domain.model.trip.PreSetTripTitle
import com.teamtripdraw.android.domain.model.trip.PreviewTrip

fun DataTrip.toDomainRoute(): Route =
    Route(route.map { it.toDomain() })

fun PreSetTripTitle.toData(): DataPreSetTripTitle =
    DataPreSetTripTitle(name, status)

fun DataPreviewTrip.toDomain(): PreviewTrip =
    PreviewTrip(id, name, imageUrl, routeImageUrl)

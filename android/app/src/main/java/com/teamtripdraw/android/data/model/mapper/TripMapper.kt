package com.teamtripdraw.android.data.model.mapper

import com.teamtripdraw.android.data.model.DataTrip
import com.teamtripdraw.android.domain.model.point.Route

fun DataTrip.toDomainRoute(): Route =
    Route(route.map { it.toDomain() })

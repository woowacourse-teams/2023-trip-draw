package com.teamtripdraw.android.ui.model.mapper

import com.teamtripdraw.android.domain.model.point.Route
import com.teamtripdraw.android.ui.model.UiRoute

fun Route.toUi(): UiRoute =
    UiRoute(
        value = value.map { it.toUi() }
    )

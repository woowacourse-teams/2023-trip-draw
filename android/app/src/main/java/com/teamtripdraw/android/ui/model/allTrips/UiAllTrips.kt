package com.teamtripdraw.android.ui.model.allTrips

data class UiAllTrips(
    val tripItems: List<UiAllTripItem>,
    val isEmpty: Boolean = tripItems.isEmpty(),
)

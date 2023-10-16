package com.teamtripdraw.android.ui.model.allTrips

data class UiAllTrips(
    val tripItems: List<UiTripOfAll>,
    val isEmpty: Boolean = tripItems.isEmpty(),
)

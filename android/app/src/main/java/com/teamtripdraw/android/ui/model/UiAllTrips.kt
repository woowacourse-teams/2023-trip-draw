package com.teamtripdraw.android.ui.model

data class UiAllTrips(
    val tripItems: List<UiTripOfAll>,
    val isEmpty: Boolean = tripItems.isEmpty(),
)

package com.teamtripdraw.android.ui.model

data class UiTripHistory(
    val previewTrips: List<UiPreviewTrip>,
    val isEmpty: Boolean = previewTrips.isEmpty(),
)

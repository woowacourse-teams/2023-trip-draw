package com.teamtripdraw.android.data.model

data class DataTripOfAll(
    val tripId: Long,
    val name: String,
    val imageUrl: String,
    val routeImageUrl: String,
    val startTime: String,
    val endTime: String,
    val isMine: Boolean,
    val authorNickname: String,
)

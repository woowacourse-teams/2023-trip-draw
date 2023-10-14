package com.teamtripdraw.android.data.model

data class DataTrip(
    val tripId: Long,
    val name: String,
    val route: List<DataPoint>,
    val status: String,
    val imageUrl: String,
    val routeImageUrl: String,
    val isMine: Boolean,
    val authorNickname: String,
)

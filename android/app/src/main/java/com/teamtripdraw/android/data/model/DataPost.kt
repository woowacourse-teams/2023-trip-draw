package com.teamtripdraw.android.data.model

data class DataPost(
    val postId: Long,
    val tripId: Long,
    val title: String,
    val writing: String,
    val address: String,
    val point: DataPoint,
    val postImageUrl: String?,
    val routeImageUrl: String?
)

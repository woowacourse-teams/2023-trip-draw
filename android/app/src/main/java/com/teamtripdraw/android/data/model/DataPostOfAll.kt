package com.teamtripdraw.android.data.model

data class DataPostOfAll(
    val postId: Long,
    val tripId: Long,
    val title: String,
    val writing: String,
    val address: String,
    val postImageUrl: String,
    val routeImageUrl: String,
    val recordedAt: String,
    val memberNickname: String,
)

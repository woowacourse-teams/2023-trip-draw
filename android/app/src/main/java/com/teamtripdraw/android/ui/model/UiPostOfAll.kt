package com.teamtripdraw.android.ui.model

data class UiPostOfAll(
    val postId: Long,
    val tripId: Long,
    val title: String,
    val writing: String,
    val address: String,
    val postImageUrl: String,
    val routeImageUrl: String,
    val recordedAt: String,
)

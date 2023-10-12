package com.teamtripdraw.android.domain.model.post

import java.time.LocalDateTime

data class PostOfAll(
    val postId: Long,
    val tripId: Long,
    val title: String,
    val writing: String,
    val address: String,
    val postImageUrl: String,
    val routeImageUrl: String,
    val recordedAt: LocalDateTime,
    val memberNickname: String,
)

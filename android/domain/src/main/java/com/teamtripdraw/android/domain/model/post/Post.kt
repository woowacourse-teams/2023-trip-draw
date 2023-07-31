package com.teamtripdraw.android.domain.model.post

import com.teamtripdraw.android.domain.model.point.Point

data class Post(
    val postId: Long,
    val tripId: Long,
    val title: String,
    val writing: String,
    val address: String,
    val point: Point,
    val postImageUrl: String?,
    val routeImageUrl: String?
)

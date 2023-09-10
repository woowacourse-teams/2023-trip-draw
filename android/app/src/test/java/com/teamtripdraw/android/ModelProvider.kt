package com.teamtripdraw.android

import com.teamtripdraw.android.domain.model.point.Point
import com.teamtripdraw.android.domain.model.post.Post
import java.time.LocalDateTime

fun getPost(
    postId: Long = 0,
    tripId: Long = 0,
    title: String = "",
    writing: String = "",
    address: String = "",
    point: Point = getPoint(),
    postImageUrl: String = "",
    routeImageUrl: String = "",
): Post = Post(
    postId = postId,
    tripId = tripId,
    title = title,
    writing = writing,
    address = address,
    point = point,
    postImageUrl = postImageUrl,
    routeImageUrl = routeImageUrl,
)

fun getPoint(
    pointId: Long = 0,
    latitude: Double = 0.0,
    longitude: Double = 0.0,
    hasPost: Boolean = false,
    recordedAt: LocalDateTime = LocalDateTime.of(2023, 8, 2, 7, 40),
): Point = Point(
    pointId = pointId,
    latitude = latitude,
    longitude = longitude,
    hasPost = hasPost,
    recordedAt = recordedAt,
)

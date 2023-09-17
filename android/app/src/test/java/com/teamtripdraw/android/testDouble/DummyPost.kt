package com.teamtripdraw.android.testDouble

import com.teamtripdraw.android.domain.model.point.Point
import com.teamtripdraw.android.domain.model.post.Post
import com.teamtripdraw.android.domain.model.post.PrePost
import java.io.File

internal fun DummyPost(
    postId: Long = 0,
    tripId: Long = 0,
    title: String = "",
    writing: String = "",
    address: String = "",
    point: Point = DummyPoint(),
    postImageUrl: String? = null,
    routeImageUrl: String? = null,
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

internal fun DummyPrePost(
    tripId: Long = 0,
    pointId: Long = 0,
    title: String = "",
    writing: String = "",
    address: String = "",
    imageFile: File? = null,
): PrePost = PrePost(
    tripId = tripId,
    pointId = pointId,
    title = title,
    writing = writing,
    address = address,
    imageFile = imageFile,
)

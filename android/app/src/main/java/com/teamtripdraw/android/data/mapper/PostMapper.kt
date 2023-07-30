package com.teamtripdraw.android.data.mapper

import com.teamtripdraw.android.data.httpClient.dto.response.CurrentPointPostResponse
import com.teamtripdraw.android.data.httpClient.dto.response.PointResponse
import com.teamtripdraw.android.data.httpClient.dto.response.PostResponse
import com.teamtripdraw.android.data.httpClient.dto.response.SelectedPointPostResponse
import com.teamtripdraw.android.domain.point.Point
import com.teamtripdraw.android.domain.post.Post
import java.time.LocalDateTime

fun CurrentPointPostResponse.toDomain(): Long {
    return postId
}

fun SelectedPointPostResponse.toDomain(): Long {
    return postId
}

fun PointResponse.toDomain(): Point {
    return Point(
        pointId = pointId,
        latitude = latitude,
        longitude = longitude,
        recordedAt = LocalDateTime.parse(recordedAt)
    )
}

fun PostResponse.toDomain(): Post {
    return Post(
        postId = postId,
        tripId = tripId,
        title = title,
        writing = writing,
        address = address,
        point = pointResponse.toDomain(),
        postImageUrl = postImageUrl,
        routeImageUrl = routeImageUrl

    )
}
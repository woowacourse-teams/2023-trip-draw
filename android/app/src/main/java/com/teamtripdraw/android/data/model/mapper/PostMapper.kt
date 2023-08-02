package com.teamtripdraw.android.data.model.mapper

import com.teamtripdraw.android.data.httpClient.dto.response.PostPointResponse
import com.teamtripdraw.android.data.httpClient.dto.response.PostDetailResponse
import com.teamtripdraw.android.data.httpClient.dto.response.AddPostResponse
import com.teamtripdraw.android.data.model.DataPoint
import com.teamtripdraw.android.data.model.DataPost
import com.teamtripdraw.android.data.model.DataPrePost
import com.teamtripdraw.android.domain.model.point.Point
import com.teamtripdraw.android.domain.model.post.Post
import com.teamtripdraw.android.domain.model.post.PrePost
import java.time.LocalDateTime

fun AddPostResponse.toData(): Long {
    return postId
}

fun PostPointResponse.toData(): DataPoint {
    return DataPoint(
        pointId = pointId,
        latitude = latitude,
        longitude = longitude,
        hasPost = hasPost,
        recordedAt = recordedAt
    )
}

fun PostDetailResponse.toData(): DataPost {
    return DataPost(
        postId = postId,
        tripId = tripId,
        title = title,
        writing = writing,
        address = address,
        point = point.toData(),
        postImageUrl = postImageUrl,
        routeImageUrl = routeImageUrl
    )
}

fun PrePost.toData(): DataPrePost {
    return DataPrePost(
        tripId = tripId,
        pointId = pointId,
        title = title,
        writing = writing,
        address = address
    )
}

fun DataPost.toDomain(): Post {
    return Post(
        postId = postId,
        tripId = tripId,
        title = title,
        writing = writing,
        address = address,
        point = point.toDomain(),
        postImageUrl = postImageUrl,
        routeImageUrl = routeImageUrl
    )
}

fun DataPoint.toDomain(): Point {
    return Point(
        pointId = pointId,
        latitude = latitude,
        longitude = longitude,
        hasPost = hasPost,
        recordedAt = LocalDateTime.parse(recordedAt)
    )
}

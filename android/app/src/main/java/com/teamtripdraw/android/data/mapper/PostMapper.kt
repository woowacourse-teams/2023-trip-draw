package com.teamtripdraw.android.data.mapper

import com.teamtripdraw.android.data.httpClient.dto.response.AddCurrentPointPostResponse
import com.teamtripdraw.android.data.httpClient.dto.response.GetPostPointResponse
import com.teamtripdraw.android.data.httpClient.dto.response.GetPostResponse
import com.teamtripdraw.android.data.httpClient.dto.response.AddSelectedPointPostResponse
import com.teamtripdraw.android.data.model.DataPoint
import com.teamtripdraw.android.data.model.DataPost
import com.teamtripdraw.android.domain.model.point.Point
import com.teamtripdraw.android.domain.model.post.Post
import java.time.LocalDateTime

fun AddCurrentPointPostResponse.toData(): Long {
    return postId
}

fun AddSelectedPointPostResponse.toData(): Long {
    return postId
}

fun GetPostPointResponse.toData(): DataPoint {
    return DataPoint(
        pointId = pointId,
        latitude = latitude,
        longitude = longitude,
        recordedAt = recordedAt
    )
}

fun GetPostResponse.toData(): DataPost {
    return DataPost(
        postId = postId,
        tripId = tripId,
        title = title,
        writing = writing,
        address = address,
        point = getPostPointResponse.toData(),
        postImageUrl = postImageUrl,
        routeImageUrl = routeImageUrl
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
        recordedAt = LocalDateTime.parse(recordedAt)
    )
}

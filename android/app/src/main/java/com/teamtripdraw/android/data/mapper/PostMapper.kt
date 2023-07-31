package com.teamtripdraw.android.data.mapper

import com.teamtripdraw.android.data.httpClient.dto.response.AddCurrentPointPostResponse
import com.teamtripdraw.android.data.httpClient.dto.response.GetPostPointResponse
import com.teamtripdraw.android.data.httpClient.dto.response.GetPostResponse
import com.teamtripdraw.android.data.httpClient.dto.response.AddSelectedPointPostResponse
import com.teamtripdraw.android.domain.point.Point
import com.teamtripdraw.android.domain.post.Post
import java.time.LocalDateTime

fun AddCurrentPointPostResponse.toDomain(): Long {
    return postId
}

fun AddSelectedPointPostResponse.toDomain(): Long {
    return postId
}

fun GetPostPointResponse.toDomain(): Point {
    return Point(
        pointId = pointId,
        latitude = latitude,
        longitude = longitude,
        recordedAt = LocalDateTime.parse(recordedAt)
    )
}

fun GetPostResponse.toDomain(): Post {
    return Post(
        postId = postId,
        tripId = tripId,
        title = title,
        writing = writing,
        address = address,
        point = getPostPointResponse.toDomain(),
        postImageUrl = postImageUrl,
        routeImageUrl = routeImageUrl
    )
}

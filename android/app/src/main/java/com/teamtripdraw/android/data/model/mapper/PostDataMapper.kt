package com.teamtripdraw.android.data.model.mapper

import com.teamtripdraw.android.data.httpClient.dto.response.AddPostResponse
import com.teamtripdraw.android.data.httpClient.dto.response.GetPostPointResponse
import com.teamtripdraw.android.data.httpClient.dto.response.GetTripPostListResponse
import com.teamtripdraw.android.data.httpClient.dto.response.GetTripPostResponse
import com.teamtripdraw.android.data.model.DataPoint
import com.teamtripdraw.android.data.model.DataPost
import com.teamtripdraw.android.data.model.DataPrePatchPost
import com.teamtripdraw.android.data.model.DataPrePost
import com.teamtripdraw.android.domain.model.post.Post
import com.teamtripdraw.android.domain.model.post.PrePatchPost
import com.teamtripdraw.android.domain.model.post.PrePost

fun AddPostResponse.toData(): Long {
    return postId
}

fun GetPostPointResponse.toData(): DataPoint {
    return DataPoint(
        pointId = pointId,
        latitude = latitude,
        longitude = longitude,
        hasPost = hasPost,
        recordedAt = recordedAt,
    )
}

fun GetTripPostResponse.toData(): DataPost {
    return DataPost(
        postId = postId,
        tripId = tripId,
        title = title,
        writing = writing,
        address = address,
        point = point.toData(),
        postImageUrl = postImageUrl,
        routeImageUrl = routeImageUrl,
    )
}

fun GetTripPostListResponse.toData(): List<DataPost> =
    this.posts.map { it.toData() }

fun PrePost.toData(): DataPrePost {
    return DataPrePost(
        tripId = tripId,
        pointId = pointId,
        title = title,
        writing = writing,
        address = address,
    )
}

fun PrePatchPost.toData(): DataPrePatchPost {
    return DataPrePatchPost(
        postId = postId,
        title = title,
        writing = writing,
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
        routeImageUrl = routeImageUrl,
    )
}

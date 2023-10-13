package com.teamtripdraw.android.data.httpClient.dto.mapper

import com.teamtripdraw.android.data.httpClient.dto.request.AddPostRequest
import com.teamtripdraw.android.data.httpClient.dto.request.PatchPostRequest
import com.teamtripdraw.android.data.httpClient.dto.response.AddPostResponse
import com.teamtripdraw.android.data.httpClient.dto.response.GetAllPostsResponse
import com.teamtripdraw.android.data.httpClient.dto.response.GetPointResponse
import com.teamtripdraw.android.data.httpClient.dto.response.GetPostPointResponse
import com.teamtripdraw.android.data.httpClient.dto.response.GetTripPostListResponse
import com.teamtripdraw.android.data.httpClient.dto.response.GetTripPostResponse
import com.teamtripdraw.android.data.model.DataPoint
import com.teamtripdraw.android.data.model.DataPost
import com.teamtripdraw.android.data.model.DataPostOfAll
import com.teamtripdraw.android.data.model.DataPrePatchPost
import com.teamtripdraw.android.data.model.DataPrePost

fun DataPrePost.toHttpRequest(): AddPostRequest {
    return AddPostRequest(
        tripId = tripId,
        pointId = pointId,
        title = title,
        writing = writing,
        address = address,
    )
}

fun DataPrePatchPost.toHttpRequest(): PatchPostRequest {
    return PatchPostRequest(
        title = title,
        writing = writing,
    )
}

fun GetPointResponse.toData(): DataPoint {
    return DataPoint(
        pointId = pointId,
        latitude = latitude,
        longitude = longitude,
        hasPost = hasPost,
        recordedAt = recordedAt,
    )
}

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
        isMine = isMine,
        authorNickname = authorNickname,
    )
}

fun GetTripPostListResponse.toData(): List<DataPost> =
    this.posts.map { it.toData() }

fun GetAllPostsResponse.toData(): List<DataPostOfAll> {
    val posts = this.posts.map {
        DataPostOfAll(
            postId = it.postId,
            tripId = it.tripId,
            title = it.title,
            writing = it.writing,
            address = it.address,
            postImageUrl = it.postImageUrl,
            routeImageUrl = it.routeImageUrl,
            recordedAt = it.recordedAt,
        )
    }
    return posts
}

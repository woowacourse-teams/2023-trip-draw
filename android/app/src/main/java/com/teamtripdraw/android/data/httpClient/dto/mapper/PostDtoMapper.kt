package com.teamtripdraw.android.data.httpClient.dto.mapper

import com.teamtripdraw.android.data.httpClient.dto.request.AddPostRequest
import com.teamtripdraw.android.data.httpClient.dto.request.PatchPostRequest
import com.teamtripdraw.android.data.httpClient.dto.response.GetAllPostsResponse
import com.teamtripdraw.android.data.httpClient.dto.response.GetPointResponse
import com.teamtripdraw.android.data.model.DataPoint
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

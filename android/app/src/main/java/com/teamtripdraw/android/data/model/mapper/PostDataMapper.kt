package com.teamtripdraw.android.data.model.mapper

import com.teamtripdraw.android.data.model.DataPost
import com.teamtripdraw.android.data.model.DataPostOfAll
import com.teamtripdraw.android.data.model.DataPrePatchPost
import com.teamtripdraw.android.data.model.DataPrePost
import com.teamtripdraw.android.domain.model.post.Post
import com.teamtripdraw.android.domain.model.post.PostOfAll
import com.teamtripdraw.android.domain.model.post.PrePatchPost
import com.teamtripdraw.android.domain.model.post.PrePost
import java.time.LocalDateTime

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

fun DataPostOfAll.toDomain(): PostOfAll {
    return PostOfAll(
        postId = postId,
        tripId = tripId,
        title = title,
        writing = writing,
        address = address,
        postImageUrl = postImageUrl,
        routeImageUrl = routeImageUrl,
        recordedAt = LocalDateTime.parse(recordedAt),
        memberNickname = memberNickname,
    )
}

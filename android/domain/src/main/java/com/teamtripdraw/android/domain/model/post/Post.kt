package com.teamtripdraw.android.domain.model.post

import com.teamtripdraw.android.domain.model.point.Point

data class Post(
    val postId: Long,
    val tripId: Long,
    val title: String,
    val writing: String,
    val address: String,
    val point: Point,
    val postImageUrl: String?,
    val routeImageUrl: String?
) {

    companion object {

        fun getPreSelectedPointPost(
            tripId: Long,
            pointId: Long,
            title: String,
            writing: String,
            address: String
        ): PreSelectedPointPost {
            return PreSelectedPointPost(
                tripId = tripId,
                pointId = pointId,
                title = title,
                writing = writing,
                address = address
            )
        }
    }
}

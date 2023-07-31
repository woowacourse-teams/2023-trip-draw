package com.teamtripdraw.android.data.dataSource.post

import com.teamtripdraw.android.data.model.DataPost
import java.time.LocalDateTime

interface PostDataSource {

    interface Local

    interface Remote {
        suspend fun addCurrentPointPost(
            tripId: Long,
            title: String,
            address: String,
            writing: String,
            latitude: Double,
            longitude: Double,
            recordedAt: LocalDateTime
        ): Result<Long>

        suspend fun addSelectedPointPost(
            tripId: Long,
            pointId: Long,
            title: String,
            address: String,
            writing: String
        ): Result<Long>

        suspend fun getPost(postId: Long): Result<DataPost>

        suspend fun getAllPosts(tripId: Long): Result<List<DataPost>>

        suspend fun deletePost(postId: Long): Result<Unit>
    }
}

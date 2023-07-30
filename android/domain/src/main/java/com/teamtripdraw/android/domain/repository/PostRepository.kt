package com.teamtripdraw.android.domain.repository

import com.teamtripdraw.android.domain.post.Post
import java.time.LocalDateTime

interface PostRepository {
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

    suspend fun getPost(postId: Long): Result<Post>

    suspend fun getAllPosts(tripId: Long): Result<List<Post>>

    suspend fun deletePost(postId: Long): Result<Unit>

}
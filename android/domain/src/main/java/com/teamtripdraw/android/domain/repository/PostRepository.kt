package com.teamtripdraw.android.domain.repository

import com.teamtripdraw.android.domain.model.post.Post
import com.teamtripdraw.android.domain.model.post.PreCurrentPointPost
import com.teamtripdraw.android.domain.model.post.PreSelectedPointPost
import java.time.LocalDateTime

interface PostRepository {
    suspend fun addCurrentPointPost(
        preCurrentPointPost: PreCurrentPointPost
    ): Result<Long>

    suspend fun addSelectedPointPost(
        preSelectedPointPost: PreSelectedPointPost
    ): Result<Long>

    suspend fun getPost(postId: Long): Result<Post>

    suspend fun getAllPosts(tripId: Long): Result<List<Post>>

    suspend fun deletePost(postId: Long): Result<Unit>

}

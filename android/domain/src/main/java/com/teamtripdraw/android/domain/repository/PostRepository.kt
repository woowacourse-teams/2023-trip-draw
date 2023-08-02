package com.teamtripdraw.android.domain.repository

import com.teamtripdraw.android.domain.model.post.Post
import com.teamtripdraw.android.domain.model.post.PreSelectedPointPost

interface PostRepository {

    suspend fun addSelectedPointPost(
        preSelectedPointPost: PreSelectedPointPost
    ): Result<Long>

    suspend fun getPost(postId: Long): Result<Post>

    suspend fun getAllPosts(tripId: Long): Result<List<Post>>

    suspend fun deletePost(postId: Long): Result<Unit>

}

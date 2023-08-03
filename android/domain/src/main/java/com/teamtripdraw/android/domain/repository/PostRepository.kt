package com.teamtripdraw.android.domain.repository

import com.teamtripdraw.android.domain.model.post.Post
import com.teamtripdraw.android.domain.model.post.PrePost

interface PostRepository {

    suspend fun addPost(prePost: PrePost): Result<Long>

    suspend fun getPost(postId: Long): Result<Post>

    suspend fun getAllPosts(tripId: Long): Result<List<Post>>

    suspend fun deletePost(postId: Long): Result<Unit>

}

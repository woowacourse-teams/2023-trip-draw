package com.teamtripdraw.android.data.repository

import com.teamtripdraw.android.data.dataSource.post.PostDataSource
import com.teamtripdraw.android.data.mapper.toData
import com.teamtripdraw.android.data.mapper.toDomain
import com.teamtripdraw.android.domain.model.post.Post
import com.teamtripdraw.android.domain.model.post.PreCurrentPointPost
import com.teamtripdraw.android.domain.model.post.PreSelectedPointPost
import com.teamtripdraw.android.domain.repository.PostRepository
import retrofit2.Retrofit

class PostRepositoryImpl(
    private val localPostDataSource: PostDataSource.Local,
    private val remotePostDataSource: PostDataSource.Remote,
    private val retrofit: Retrofit
) : PostRepository {

    override suspend fun addCurrentPointPost(
        preCurrentPointPost: PreCurrentPointPost
    ): Result<Long> {
        return remotePostDataSource.addCurrentPointPost(
            preCurrentPointPost.toData()
        )
    }

    override suspend fun addSelectedPointPost(
        preSelectedPointPost: PreSelectedPointPost
    ): Result<Long> {
        return remotePostDataSource.addSelectedPointPost(
            preSelectedPointPost.toData()
        )
    }

    override suspend fun getPost(postId: Long): Result<Post> {
        return remotePostDataSource.getPost(postId).map { it.toDomain() }
    }

    override suspend fun getAllPosts(tripId: Long): Result<List<Post>> {
        return remotePostDataSource.getAllPosts(tripId)
            .map { dataPosts -> dataPosts.map { it.toDomain() } }
    }

    override suspend fun deletePost(postId: Long): Result<Unit> {
        return remotePostDataSource.deletePost(postId)
    }
}

package com.teamtripdraw.android.data.repository

import com.teamtripdraw.android.data.dataSource.post.PostDataSource
import com.teamtripdraw.android.data.httpClient.dto.request.AddCurrentPointPostRequest
import com.teamtripdraw.android.data.httpClient.dto.request.AddSelectedPointPostRequest
import com.teamtripdraw.android.data.mapper.toDomain
import com.teamtripdraw.android.domain.model.post.Post
import com.teamtripdraw.android.domain.repository.PostRepository
import retrofit2.Retrofit
import java.time.LocalDateTime

class PostRepositoryImpl(
    private val localPostDataSource: PostDataSource.Local,
    private val remotePostDataSource: PostDataSource.Remote,
    private val retrofit: Retrofit
) : PostRepository {

    override suspend fun addCurrentPointPost(
        tripId: Long,
        title: String,
        address: String,
        writing: String,
        latitude: Double,
        longitude: Double,
        recordedAt: LocalDateTime
    ): Result<Long> {
        val addCurrentPointPostRequest = AddCurrentPointPostRequest(
            tripId = tripId,
            title = title,
            address = address,
            writing = writing,
            latitude = latitude,
            longitude = longitude,
            recordedAt = recordedAt.toString()
        )
        return remotePostDataSource.addCurrentPointPost(addCurrentPointPostRequest)
            .process { body, headers ->
                Result.success(body.toDomain())
            }
    }

    override suspend fun addSelectedPointPost(
        tripId: Long,
        pointId: Long,
        title: String,
        address: String,
        writing: String
    ): Result<Long> {
        val addSelectedPointPostRequest = AddSelectedPointPostRequest(
            tripId = tripId, pointId = pointId, title = title, address = address, writing = writing
        )
        return remotePostDataSource.addSelectedPointPost(addSelectedPointPostRequest)
            .process { body, headers ->
                Result.success(body.toDomain())
            }
    }

    override suspend fun getPost(postId: Long): Result<Post> {
        return remotePostDataSource.getPost(postId).process { body, headers ->
            Result.success(body.toDomain())
        }
    }

    override suspend fun getAllPosts(tripId: Long): Result<List<Post>> {
        return remotePostDataSource.getAllPosts(tripId).process { body, headers ->
            Result.success(body.map { it.toDomain() })
        }
    }

    override suspend fun deletePost(postId: Long): Result<Unit> {
        return remotePostDataSource.getAllPosts(postId).process { body, headers ->
            Result.success(Unit)
        }
    }
}

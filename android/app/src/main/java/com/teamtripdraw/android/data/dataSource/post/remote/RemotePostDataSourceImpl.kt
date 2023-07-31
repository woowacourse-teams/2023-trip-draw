package com.teamtripdraw.android.data.dataSource.post.remote

import com.teamtripdraw.android.data.dataSource.post.PostDataSource
import com.teamtripdraw.android.data.httpClient.dto.request.AddCurrentPointPostRequest
import com.teamtripdraw.android.data.httpClient.dto.request.AddSelectedPointPostRequest
import com.teamtripdraw.android.data.httpClient.service.PostService
import com.teamtripdraw.android.data.mapper.toData
import com.teamtripdraw.android.data.model.DataPost
import java.time.LocalDateTime

class RemotePostDataSourceImpl(private val postService: PostService) : PostDataSource.Remote {
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

        return postService.addCurrentPointPost(addCurrentPointPostRequest)
            .process { body, headers ->
                Result.success(body.toData())
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

        return postService.addSelectedPointPost(addSelectedPointPostRequest)
            .process { body, headers ->
                Result.success(body.toData())
            }
    }

    override suspend fun getPost(postId: Long): Result<DataPost> {
        return postService.getPost(postId).process { body, headers ->
            Result.success(body.toData())
        }
    }

    override suspend fun getAllPosts(tripId: Long): Result<List<DataPost>> {
        return postService.getAllPosts(tripId).process { body, headers ->
            Result.success(body.map { it.toData() })
        }
    }

    override suspend fun deletePost(postId: Long): Result<Unit> {
        return postService.deletePost(postId).process { body, headers ->
            Result.success(body)
        }
    }
}

package com.teamtripdraw.android.data.dataSource.post.remote

import com.teamtripdraw.android.data.dataSource.post.PostDataSource
import com.teamtripdraw.android.data.httpClient.dto.mapper.toHttpRequest
import com.teamtripdraw.android.data.httpClient.service.PostService
import com.teamtripdraw.android.data.model.mapper.toData
import com.teamtripdraw.android.data.model.DataPost
import com.teamtripdraw.android.data.model.DataPrePost

class RemotePostDataSourceImpl(private val postService: PostService) : PostDataSource.Remote {

    override suspend fun addPost(
        dataPrePost: DataPrePost
    ): Result<Long> {
        return postService.addPost(dataPrePost.toHttpRequest())
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
            Result.success(body.toData())
        }
    }

    override suspend fun deletePost(postId: Long): Result<Unit> {
        return postService.deletePost(postId).process { body, headers ->
            Result.success(body)
        }
    }
}

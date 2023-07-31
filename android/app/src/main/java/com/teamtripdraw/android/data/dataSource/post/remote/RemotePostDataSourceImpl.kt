package com.teamtripdraw.android.data.dataSource.post.remote

import com.teamtripdraw.android.data.dataSource.post.PostDataSource
import com.teamtripdraw.android.data.httpClient.dto.request.AddCurrentPointPostRequest
import com.teamtripdraw.android.data.httpClient.dto.request.AddSelectedPointPostRequest
import com.teamtripdraw.android.data.httpClient.dto.response.AddCurrentPointPostResponse
import com.teamtripdraw.android.data.httpClient.dto.response.GetPostResponse
import com.teamtripdraw.android.data.httpClient.dto.response.AddSelectedPointPostResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.ResponseState
import com.teamtripdraw.android.data.httpClient.service.PostService

class RemotePostDataSourceImpl(private val postService: PostService) : PostDataSource.Remote {
    override suspend fun addCurrentPointPost(addCurrentPointPostRequest: AddCurrentPointPostRequest): ResponseState<AddCurrentPointPostResponse> =
        postService.addCurrentPointPost(addCurrentPointPostRequest)

    override suspend fun addSelectedPointPost(addSelectedPointPostRequest: AddSelectedPointPostRequest): ResponseState<AddSelectedPointPostResponse> =
        postService.addSelectedPointPost(addSelectedPointPostRequest)

    override suspend fun getPost(postId: Long): ResponseState<GetPostResponse> =
        postService.getPost(postId)

    override suspend fun getAllPosts(tripId: Long): ResponseState<List<GetPostResponse>> =
        postService.getAllPosts(tripId)

    override suspend fun deletePost(postId: Long): ResponseState<Unit> =
        postService.deletePost(postId)
}

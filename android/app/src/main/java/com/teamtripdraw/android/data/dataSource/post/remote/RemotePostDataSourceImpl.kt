package com.teamtripdraw.android.data.dataSource.post.remote

import com.teamtripdraw.android.data.dataSource.PostDataSource
import com.teamtripdraw.android.data.httpClient.dto.request.CurrentPointPostRequest
import com.teamtripdraw.android.data.httpClient.dto.request.SelectedPointPostRequest
import com.teamtripdraw.android.data.httpClient.dto.response.CurrentPointPostResponse
import com.teamtripdraw.android.data.httpClient.dto.response.PostResponse
import com.teamtripdraw.android.data.httpClient.dto.response.SelectedPointPostResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.ResponseState
import com.teamtripdraw.android.data.httpClient.service.PostService

class RemotePostDataSourceImpl(private val postService: PostService) : PostDataSource.Remote {
    override suspend fun addCurrentPointPost(currentPointPostRequest: CurrentPointPostRequest): ResponseState<CurrentPointPostResponse> =
        postService.addCurrentPointPost(currentPointPostRequest)

    override suspend fun addSelectedPointPost(selectedPointPostRequest: SelectedPointPostRequest): ResponseState<SelectedPointPostResponse> =
        postService.addSelectedPointPost(selectedPointPostRequest)

    override suspend fun getPost(postId: Long): ResponseState<PostResponse> =
        postService.getPost(postId)

    override suspend fun getAllPosts(tripId: Long): ResponseState<List<PostResponse>> =
        postService.getAllPosts(tripId)

    override suspend fun deletePost(postId: Long): ResponseState<Unit> =
        postService.deletePost(postId)
}
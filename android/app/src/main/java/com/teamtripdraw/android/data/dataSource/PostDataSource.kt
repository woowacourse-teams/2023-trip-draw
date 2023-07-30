package com.teamtripdraw.android.data.dataSource

import com.teamtripdraw.android.data.httpClient.dto.request.CurrentPointPostRequest
import com.teamtripdraw.android.data.httpClient.dto.request.SelectedPointPostRequest
import com.teamtripdraw.android.data.httpClient.dto.response.CurrentPointPostResponse
import com.teamtripdraw.android.data.httpClient.dto.response.PostResponse
import com.teamtripdraw.android.data.httpClient.dto.response.SelectedPointPostResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.ResponseState

interface PostDataSource {

    interface Local
    interface Remote {
        suspend fun addCurrentPointPost(currentPointPostRequest: CurrentPointPostRequest): ResponseState<CurrentPointPostResponse>
        suspend fun addSelectedPointPost(selectedPointPostRequest: SelectedPointPostRequest): ResponseState<SelectedPointPostResponse>
        suspend fun getPost(postId: Long): ResponseState<PostResponse>
        suspend fun getAllPosts(tripId: Long): ResponseState<List<PostResponse>>
        suspend fun deletePost(postId: Long): ResponseState<Unit>
    }

}
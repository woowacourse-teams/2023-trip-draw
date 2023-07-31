package com.teamtripdraw.android.data.dataSource.post

import com.teamtripdraw.android.data.httpClient.dto.request.AddCurrentPointPostRequest
import com.teamtripdraw.android.data.httpClient.dto.request.AddSelectedPointPostRequest
import com.teamtripdraw.android.data.httpClient.dto.response.AddCurrentPointPostResponse
import com.teamtripdraw.android.data.httpClient.dto.response.GetPostResponse
import com.teamtripdraw.android.data.httpClient.dto.response.AddSelectedPointPostResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.ResponseState

interface PostDataSource {

    interface Local
    interface Remote {
        suspend fun addCurrentPointPost(addCurrentPointPostRequest: AddCurrentPointPostRequest): ResponseState<AddCurrentPointPostResponse>
        suspend fun addSelectedPointPost(addSelectedPointPostRequest: AddSelectedPointPostRequest): ResponseState<AddSelectedPointPostResponse>
        suspend fun getPost(postId: Long): ResponseState<GetPostResponse>
        suspend fun getAllPosts(tripId: Long): ResponseState<List<GetPostResponse>>
        suspend fun deletePost(postId: Long): ResponseState<Unit>
    }
}

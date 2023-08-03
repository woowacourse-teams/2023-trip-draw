package com.teamtripdraw.android.data.httpClient.service

import com.teamtripdraw.android.data.httpClient.dto.request.AddPostRequest
import com.teamtripdraw.android.data.httpClient.dto.response.GetPostResponse
import com.teamtripdraw.android.data.httpClient.dto.response.AddPostResponse
import com.teamtripdraw.android.data.httpClient.dto.response.GetPostListResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.ResponseState
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PostService {

    @POST("/posts")
    suspend fun addPost(
        @Body addPostRequest: AddPostRequest
    ): ResponseState<AddPostResponse>

    @GET("/posts/{postId}")
    suspend fun getPost(@Path("postId") postId: Long): ResponseState<GetPostResponse>

    @GET("/trips/{tripId}/posts")
    suspend fun getAllPosts(@Path("tripId") tripId: Long): ResponseState<GetPostListResponse>

    @DELETE("/posts/{postId}")
    suspend fun deletePost(@Path("postId") postId: Long): ResponseState<Unit>
}

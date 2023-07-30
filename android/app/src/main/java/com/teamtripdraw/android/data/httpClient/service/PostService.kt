package com.teamtripdraw.android.data.httpClient.service

import com.teamtripdraw.android.data.httpClient.dto.request.CurrentPointPostRequest
import com.teamtripdraw.android.data.httpClient.dto.request.SelectedPointPostRequest
import com.teamtripdraw.android.data.httpClient.dto.response.CurrentPointPostResponse
import com.teamtripdraw.android.data.httpClient.dto.response.PostResponse
import com.teamtripdraw.android.data.httpClient.dto.response.SelectedPointPostResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.ResponseState
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PostService {
    @POST("/posts/current-location")
    suspend fun addCurrentPointPost(
        @Body currentPointPostRequest: CurrentPointPostRequest
    ): ResponseState<CurrentPointPostResponse>

    @POST("/posts")
    suspend fun addSelectedPointPost(
        @Body selectedPointPostRequest: SelectedPointPostRequest
    ): ResponseState<SelectedPointPostResponse>

    @GET("/posts/{postId}")
    suspend fun getPost(@Path("postId") postId: Long): ResponseState<PostResponse>

    @GET("/trips/{tripId}/posts")
    suspend fun getAllPosts(@Path("tripId") tripId: Long): ResponseState<List<PostResponse>>

    @DELETE("/posts/{postId}")
    suspend fun deletePost(@Path("postId") postId: Long): ResponseState<Unit>
}
package com.teamtripdraw.android.data.httpClient.service

import com.teamtripdraw.android.data.httpClient.dto.request.AddCurrentPointPostRequest
import com.teamtripdraw.android.data.httpClient.dto.request.AddSelectedPointPostRequest
import com.teamtripdraw.android.data.httpClient.dto.response.AddCurrentPointPostResponse
import com.teamtripdraw.android.data.httpClient.dto.response.GetPostResponse
import com.teamtripdraw.android.data.httpClient.dto.response.AddSelectedPointPostResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.ResponseState
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PostService {
    @POST("/posts/current-location")
    suspend fun addCurrentPointPost(
        @Body addCurrentPointPostRequest: AddCurrentPointPostRequest
    ): ResponseState<AddCurrentPointPostResponse>

    @POST("/posts")
    suspend fun addSelectedPointPost(
        @Body addSelectedPointPostRequest: AddSelectedPointPostRequest
    ): ResponseState<AddSelectedPointPostResponse>

    @GET("/posts/{postId}")
    suspend fun getPost(@Path("postId") postId: Long): ResponseState<GetPostResponse>

    @GET("/trips/{tripId}/posts")
    suspend fun getAllPosts(@Path("tripId") tripId: Long): ResponseState<List<GetPostResponse>>

    @DELETE("/posts/{postId}")
    suspend fun deletePost(@Path("postId") postId: Long): ResponseState<Unit>
}

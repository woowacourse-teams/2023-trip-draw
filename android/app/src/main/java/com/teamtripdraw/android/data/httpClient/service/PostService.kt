package com.teamtripdraw.android.data.httpClient.service

import com.teamtripdraw.android.data.httpClient.dto.request.GetAllPostsRequest
import com.teamtripdraw.android.data.httpClient.dto.response.AddPostResponse
import com.teamtripdraw.android.data.httpClient.dto.response.GetAllPostsResponse
import com.teamtripdraw.android.data.httpClient.dto.response.GetTripPostListResponse
import com.teamtripdraw.android.data.httpClient.dto.response.GetTripPostResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState.ResponseState
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface PostService {

    @Multipart
    @POST("/posts")
    suspend fun addPost(
        @Part("dto") addPostRequest: RequestBody,
        @Part imageFile: MultipartBody.Part?,
    ): ResponseState<AddPostResponse>

    @GET("/posts/{postId}")
    suspend fun getPost(@Path("postId") postId: Long): ResponseState<GetTripPostResponse>

    @GET("/trips/{tripId}/posts")
    suspend fun getTripPosts(@Path("tripId") tripId: Long): ResponseState<GetTripPostListResponse>

    @GET("/posts")
    suspend fun getAllPosts(@Body body: GetAllPostsRequest): ResponseState<GetAllPostsResponse>

    @DELETE("/posts/{postId}")
    suspend fun deletePost(@Path("postId") postId: Long): ResponseState<Unit>

    @Multipart
    @PATCH("/posts/{postId}")
    suspend fun patchPost(
        @Path("postId") postId: Long,
        @Part("dto") patchPostRequest: RequestBody,
        @Part imageFile: MultipartBody.Part,
    ): ResponseState<Unit>

    @Multipart
    @PATCH("/posts/{postId}")
    suspend fun patchPost(
        @Path("postId") postId: Long,
        @Part("dto") patchPostRequest: RequestBody,
    ): ResponseState<Unit>
}

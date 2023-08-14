package com.teamtripdraw.android.data.httpClient.service

import com.teamtripdraw.android.data.httpClient.dto.response.AddPostResponse
import com.teamtripdraw.android.data.httpClient.dto.response.GetPostListResponse
import com.teamtripdraw.android.data.httpClient.dto.response.GetPostResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.ResponseState
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
    suspend fun getPost(@Path("postId") postId: Long): ResponseState<GetPostResponse>

    @GET("/trips/{tripId}/posts")
    suspend fun getAllPosts(@Path("tripId") tripId: Long): ResponseState<GetPostListResponse>

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

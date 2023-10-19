package com.teamtripdraw.android.data.httpClient.service

import com.teamtripdraw.android.data.httpClient.dto.response.CreateCurrentPointPostResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState.ResponseState
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface CreateCurrentPointPostService {
    @Multipart
    @POST("/posts/current-location")
    suspend fun createCurrentPointPost(
        @Part("dto") createCurrentPointPostRequest: RequestBody,
        @Part imageFile: MultipartBody.Part?,
    ): ResponseState<CreateCurrentPointPostResponse>
}

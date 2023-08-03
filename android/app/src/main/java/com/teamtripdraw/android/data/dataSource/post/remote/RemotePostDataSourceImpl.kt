package com.teamtripdraw.android.data.dataSource.post.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.teamtripdraw.android.data.dataSource.post.PostDataSource
import com.teamtripdraw.android.data.httpClient.dto.mapper.toHttpRequest
import com.teamtripdraw.android.data.httpClient.dto.request.AddPostRequest
import com.teamtripdraw.android.data.httpClient.service.PostService
import com.teamtripdraw.android.data.model.mapper.toData
import com.teamtripdraw.android.data.model.DataPost
import com.teamtripdraw.android.data.model.DataPrePost
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class RemotePostDataSourceImpl(private val postService: PostService) : PostDataSource.Remote {

    override suspend fun addPost(
        dataPrePost: DataPrePost,
        imageFile: File?
    ): Result<Long> {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        val addPostRequest =
            moshi.adapter(AddPostRequest::class.java).toJson(dataPrePost.toHttpRequest())
        val dto = addPostRequest.toRequestBody("application/json".toMediaTypeOrNull())

        val imageRequest = imageFile?.asRequestBody("image/*".toMediaTypeOrNull())
        val imageBody = imageRequest?.let {
            MultipartBody.Part.createFormData("file", imageFile.name, it)
        }

        return postService.addPost(dto, imageBody)
            .process { body, headers ->
                Result.success(body.toData())
            }
    }

    override suspend fun getPost(postId: Long): Result<DataPost> {
        return postService.getPost(postId).process { body, headers ->
            Result.success(body.toData())
        }
    }

    override suspend fun getAllPosts(tripId: Long): Result<List<DataPost>> {
        return postService.getAllPosts(tripId).process { body, headers ->
            Result.success(body.toData())
        }
    }

    override suspend fun deletePost(postId: Long): Result<Unit> {
        return postService.deletePost(postId).process { body, headers ->
            Result.success(body)
        }
    }
}

package com.teamtripdraw.android.data.dataSource.post.remote

import com.squareup.moshi.Moshi
import com.teamtripdraw.android.data.dataSource.post.PostDataSource
import com.teamtripdraw.android.data.httpClient.dto.mapper.toHttpRequest
import com.teamtripdraw.android.data.httpClient.dto.request.AddPostRequest
import com.teamtripdraw.android.data.httpClient.dto.request.PatchPostRequest
import com.teamtripdraw.android.data.httpClient.service.PostService
import com.teamtripdraw.android.data.model.DataPost
import com.teamtripdraw.android.data.model.DataPrePatchPost
import com.teamtripdraw.android.data.model.DataPrePost
import com.teamtripdraw.android.data.model.mapper.toData
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class RemotePostDataSourceImpl @Inject constructor(
    private val moshi: Moshi,
    private val postService: PostService,
) : PostDataSource.Remote {

    override suspend fun addPost(
        dataPrePost: DataPrePost,
        imageFile: File?,
    ): Result<Long> {
        val addPostRequest =
            moshi.adapter(AddPostRequest::class.java).toJson(dataPrePost.toHttpRequest())
        val dto = addPostRequest.toRequestBody("application/json".toMediaTypeOrNull())
        val imageBody = imageFile?.toMultipartRequestBody()

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

    override suspend fun patchPost(
        dataPrePatchPost: DataPrePatchPost,
        imageFile: File?,
    ): Result<Unit> {
        val patchPostRequest =
            moshi.adapter(PatchPostRequest::class.java).toJson(dataPrePatchPost.toHttpRequest())
        val dto = patchPostRequest.toRequestBody("application/json".toMediaTypeOrNull())

        return if (imageFile == null) {
            postService.patchPost(dataPrePatchPost.postId, dto).process { body, headers ->
                Result.success(body)
            }
        } else {
            val imageBody = imageFile.toMultipartRequestBody()
            postService.patchPost(dataPrePatchPost.postId, dto, imageBody)
                .process { body, headers ->
                    Result.success(body)
                }
        }
    }

    private fun File.toMultipartRequestBody(): MultipartBody.Part {
        return asRequestBody("image/jpeg".toMediaTypeOrNull()).let {
            MultipartBody.Part.createFormData("file", name, it)
        }
    }
}

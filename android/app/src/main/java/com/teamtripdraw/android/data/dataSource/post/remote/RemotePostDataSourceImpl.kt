package com.teamtripdraw.android.data.dataSource.post.remote

import com.squareup.moshi.Moshi
import com.teamtripdraw.android.data.dataSource.post.PostDataSource
import com.teamtripdraw.android.data.httpClient.dto.mapper.toData
import com.teamtripdraw.android.data.httpClient.dto.mapper.toHttpRequest
import com.teamtripdraw.android.data.httpClient.dto.request.AddPostRequest
import com.teamtripdraw.android.data.httpClient.dto.request.CreateCurrentPointPostRequest
import com.teamtripdraw.android.data.httpClient.dto.request.PatchPostRequest
import com.teamtripdraw.android.data.httpClient.service.CreateCurrentPointPostService
import com.teamtripdraw.android.data.httpClient.service.GetPointPostService
import com.teamtripdraw.android.data.httpClient.service.PostService
import com.teamtripdraw.android.data.model.DataPost
import com.teamtripdraw.android.data.model.DataPostOfAll
import com.teamtripdraw.android.data.model.DataPrePatchPost
import com.teamtripdraw.android.data.model.DataPrePost
import com.teamtripdraw.android.data.model.mapper.toData
import com.teamtripdraw.android.support.framework.presentation.LocalDateTimeFormatter.isoRemoveNanoSecondFormatter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.time.LocalDateTime
import javax.inject.Inject

class RemotePostDataSourceImpl @Inject constructor(
    private val moshi: Moshi,
    private val postService: PostService,
    private val getPointPostService: GetPointPostService,
    private val createCurrentPointPostService: CreateCurrentPointPostService,
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

    override suspend fun createCurrentPointPost(
        tripId: Long,
        title: String,
        address: String,
        writing: String,
        latitude: Double,
        longitude: Double,
        recordedAt: LocalDateTime,
        imageFile: File?,
    ): Result<Long> {
        val createCurrentPointPostRequest = CreateCurrentPointPostRequest(
            address = address,
            latitude = latitude,
            longitude = longitude,
            recordedAt = recordedAt.format(isoRemoveNanoSecondFormatter),
            title = title,
            tripId = tripId,
            writing = writing,
        )

        val request = moshi.adapter(CreateCurrentPointPostRequest::class.java)
            .toJson(createCurrentPointPostRequest)
        val dto = request.toRequestBody("application/json".toMediaTypeOrNull())
        val imageBody = imageFile?.toMultipartRequestBody()
        return createCurrentPointPostService.createCurrentPointPost(dto, imageBody)
            .process { body, headers ->
                Result.success(body.postId)
            }
    }

    override suspend fun getPostByPostId(postId: Long): Result<DataPost> {
        return postService.getPost(postId).process { body, headers ->
            Result.success(body.toData())
        }
    }

    override suspend fun getPostByPointId(pointId: Long): Result<DataPost> =
        getPointPostService.getPost(pointId)
            .process { body, headers -> Result.success(body.toData()) }

    override suspend fun getTripPosts(tripId: Long): Result<List<DataPost>> {
        return postService.getTripPosts(tripId).process { body, headers ->
            Result.success(body.toData())
        }
    }

    override suspend fun getAllPosts(
        address: String,
        ageRanges: List<Int>,
        latitude: Double?,
        longitude: Double?,
        daysOfWeek: List<Int>,
        genders: List<Int>,
        hours: List<Int>,
        months: List<Int>,
        years: List<Int>,
        lastViewedId: Long?,
        limit: Int,
    ): Result<List<DataPostOfAll>> {
        return postService.getAllPosts(
            years = years,
            months = months,
            daysOfWeek = daysOfWeek,
            hours = hours,
            ageRanges = ageRanges,
            genders = genders,
            address = address,
            lastViewedId = lastViewedId,
            limit = limit,
        ).process { body, headers ->
            Result.success(body).map { it.toData() }
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

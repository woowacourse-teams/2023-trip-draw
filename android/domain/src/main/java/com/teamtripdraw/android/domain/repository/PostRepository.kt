package com.teamtripdraw.android.domain.repository

import com.teamtripdraw.android.domain.model.post.Post
import com.teamtripdraw.android.domain.model.post.PostOfAll
import com.teamtripdraw.android.domain.model.post.PrePatchPost
import com.teamtripdraw.android.domain.model.post.PrePost
import java.io.File
import java.time.LocalDateTime

interface PostRepository {

    suspend fun addPost(prePost: PrePost): Result<Long>

    suspend fun createCurrentPointPost(
        tripId: Long,
        title: String,
        address: String,
        writing: String,
        latitude: Double,
        longitude: Double,
        recordedAt: LocalDateTime,
        imageFile: File?,
    ): Result<Long>

    suspend fun getPostByPostId(postId: Long): Result<Post>

    suspend fun getPostByPointId(pointId: Long): Result<Post>

    suspend fun getTripPosts(tripId: Long): Result<List<Post>>

    suspend fun getAllPosts(
        address: String = "",
        ageRanges: List<Int> = listOf(),
        latitude: Double? = null,
        longitude: Double? = null,
        daysOfWeek: List<Int> = listOf(),
        genders: List<Int> = listOf(),
        hours: List<Int> = listOf(),
        months: List<Int> = listOf(),
        years: List<Int> = listOf(),
        lastViewedId: Long? = null,
        limit: Int = 10,
    ): Result<List<PostOfAll>>

    suspend fun deletePost(postId: Long): Result<Unit>

    suspend fun patchPost(prePatchPost: PrePatchPost): Result<Unit>
}

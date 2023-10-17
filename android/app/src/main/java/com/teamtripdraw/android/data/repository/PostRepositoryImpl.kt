package com.teamtripdraw.android.data.repository

import com.teamtripdraw.android.data.dataSource.post.PostDataSource
import com.teamtripdraw.android.data.model.mapper.toData
import com.teamtripdraw.android.data.model.mapper.toDomain
import com.teamtripdraw.android.domain.model.post.Post
import com.teamtripdraw.android.domain.model.post.PostOfAll
import com.teamtripdraw.android.domain.model.post.PrePatchPost
import com.teamtripdraw.android.domain.model.post.PrePost
import com.teamtripdraw.android.domain.repository.PostRepository
import java.io.File
import java.time.LocalDateTime
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val remotePostDataSource: PostDataSource.Remote,
) : PostRepository {

    override suspend fun addPost(
        prePost: PrePost,
    ): Result<Long> {
        return remotePostDataSource.addPost(
            prePost.toData(),
            prePost.imageFile,
        )
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
        return remotePostDataSource.createCurrentPointPost(
            tripId = tripId,
            title = title,
            address = address,
            writing = writing,
            latitude = latitude,
            longitude = longitude,
            recordedAt = recordedAt,
            imageFile = imageFile
        )
    }

    override suspend fun getPostByPostId(postId: Long): Result<Post> {
        return remotePostDataSource.getPostByPostId(postId).map { it.toDomain() }
    }

    override suspend fun getPostByPointId(pointId: Long): Result<Post> =
        remotePostDataSource.getPostByPointId(pointId = pointId).map { it.toDomain() }

    override suspend fun getTripPosts(tripId: Long): Result<List<Post>> {
        return remotePostDataSource.getTripPosts(tripId)
            .map { dataPosts -> dataPosts.map { it.toDomain() } }
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
    ): Result<List<PostOfAll>> {
        return remotePostDataSource.getAllPosts(
            address = address,
            ageRanges = ageRanges,
            latitude = latitude,
            longitude = longitude,
            daysOfWeek = daysOfWeek,
            genders = genders,
            hours = hours,
            months = months,
            years = years,
            lastViewedId = lastViewedId,
            limit = limit,
        ).map { dataPostsOfAll -> dataPostsOfAll.map { it.toDomain() } }
    }

    override suspend fun deletePost(postId: Long): Result<Unit> {
        return remotePostDataSource.deletePost(postId)
    }

    override suspend fun patchPost(prePatchPost: PrePatchPost): Result<Unit> {
        return remotePostDataSource.patchPost(
            prePatchPost.toData(),
            prePatchPost.imageFile,
        )
    }
}

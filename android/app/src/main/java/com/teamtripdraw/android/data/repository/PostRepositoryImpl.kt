package com.teamtripdraw.android.data.repository

import com.teamtripdraw.android.data.dataSource.post.PostDataSource
import com.teamtripdraw.android.data.model.mapper.toData
import com.teamtripdraw.android.data.model.mapper.toDomain
import com.teamtripdraw.android.domain.model.post.Post
import com.teamtripdraw.android.domain.model.post.PostOfAll
import com.teamtripdraw.android.domain.model.post.PrePatchPost
import com.teamtripdraw.android.domain.model.post.PrePost
import com.teamtripdraw.android.domain.repository.PostRepository

class PostRepositoryImpl(
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

    override suspend fun getPost(postId: Long): Result<Post> {
        return remotePostDataSource.getPost(postId).map { it.toDomain() }
    }

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

package com.teamtripdraw.android.data.dataSource.post

import com.teamtripdraw.android.data.model.DataPost
import com.teamtripdraw.android.data.model.DataPostOfAll
import com.teamtripdraw.android.data.model.DataPrePatchPost
import com.teamtripdraw.android.data.model.DataPrePost
import java.io.File

interface PostDataSource {

    interface Local

    interface Remote {

        suspend fun addPost(
            dataPrePost: DataPrePost,
            imageFile: File?,
        ): Result<Long>

        suspend fun getPostByPostId(postId: Long): Result<DataPost>

        suspend fun getPostByPointId(pointId: Long): Result<DataPost>

        suspend fun getTripPosts(tripId: Long): Result<List<DataPost>>

        suspend fun getAllPosts(
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
        ): Result<List<DataPostOfAll>>

        suspend fun deletePost(postId: Long): Result<Unit>

        suspend fun patchPost(
            dataPrePatchPost: DataPrePatchPost,
            imageFile: File?,
        ): Result<Unit>
    }
}

package com.teamtripdraw.android.data.dataSource.post

import com.teamtripdraw.android.data.model.DataPost
import com.teamtripdraw.android.data.model.DataPreCurrentPointPost
import com.teamtripdraw.android.data.model.DataPreSelectedPointPost
import java.time.LocalDateTime

interface PostDataSource {

    interface Local

    interface Remote {
        suspend fun addCurrentPointPost(
            dataPreCurrentPointPost: DataPreCurrentPointPost
        ): Result<Long>

        suspend fun addSelectedPointPost(
            dataPreSelectedPointPost: DataPreSelectedPointPost
        ): Result<Long>

        suspend fun getPost(postId: Long): Result<DataPost>

        suspend fun getAllPosts(tripId: Long): Result<List<DataPost>>

        suspend fun deletePost(postId: Long): Result<Unit>
    }
}

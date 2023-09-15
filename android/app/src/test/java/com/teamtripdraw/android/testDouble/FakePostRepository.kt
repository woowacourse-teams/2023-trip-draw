package com.teamtripdraw.android.testDouble

import com.teamtripdraw.android.domain.model.post.Post
import com.teamtripdraw.android.domain.model.post.PrePatchPost
import com.teamtripdraw.android.domain.model.post.PrePost
import com.teamtripdraw.android.domain.repository.PostRepository

internal class FakePostRepository : PostRepository {
    val posts: MutableList<Post> = mutableListOf()

    override suspend fun addPost(prePost: PrePost): Result<Long> {
        val post = Post(
            postId = posts.size.toLong(),
            tripId = prePost.tripId,
            title = prePost.title,
            writing = prePost.writing,
            address = prePost.address,
            DummyPoint(pointId = prePost.pointId),
            postImageUrl = prePost.imageFile?.path ?: "",
            routeImageUrl = null,
        )
        posts.add(post)
        return Result.success(post.postId)
    }

    override suspend fun getPost(postId: Long): Result<Post> {
        val post: Post? = posts.find { it.postId == postId }
        return if (post != null) {
            Result.success(value = post)
        } else {
            Result.failure(NullPointerException())
        }
    }

    override suspend fun getAllPosts(tripId: Long): Result<List<Post>> {
        return Result.success(posts)
    }

    override suspend fun deletePost(postId: Long): Result<Unit> {
        posts.removeIf { it.postId == postId }
        return Result.success(Unit)
    }

    override suspend fun patchPost(prePatchPost: PrePatchPost): Result<Unit> {
        PrePatchPost(postId = 0, title = "", writing = "", imageFile = null)
        val beforePost = posts.find { it.postId == prePatchPost.postId }
            ?: return Result.failure(NullPointerException())
        val afterPost = Post(
            postId = beforePost.postId,
            tripId = beforePost.tripId,
            title = prePatchPost.title,
            writing = prePatchPost.writing,
            address = beforePost.address,
            point = beforePost.point,
            postImageUrl = prePatchPost.imageFile?.path ?: "",
            routeImageUrl = null,
        )

        posts.removeIf { it.postId == prePatchPost.postId }
        posts.add(afterPost)
        return Result.success(Unit)
    }
}

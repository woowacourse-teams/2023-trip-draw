package com.teamtripdraw.android.ui.model.allPosts

data class UiPostOfAll(
    val postId: Long,
    val tripId: Long,
    val title: String,
    val writing: String,
    val address: String,
    val postImageUrl: String,
    val routeImageUrl: String,
    val recordedAt: String,
    val isMine: Boolean,
    val authorNickname: String,
) : UiItemView

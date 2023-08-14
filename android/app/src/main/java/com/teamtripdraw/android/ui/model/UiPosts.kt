package com.teamtripdraw.android.ui.model

data class UiPosts(
    val postItems: List<UiPostItem>,
    val isEmpty: Boolean = postItems.isEmpty(),
)

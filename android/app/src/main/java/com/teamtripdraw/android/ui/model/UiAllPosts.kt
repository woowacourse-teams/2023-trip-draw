package com.teamtripdraw.android.ui.model

data class UiAllPosts(
    val postItems: List<UiPostOfAll>,
    val isEmpty: Boolean = postItems.isEmpty(),
)

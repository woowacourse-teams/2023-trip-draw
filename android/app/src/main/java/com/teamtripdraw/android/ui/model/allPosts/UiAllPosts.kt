package com.teamtripdraw.android.ui.model.allPosts

data class UiAllPosts(
    val postItems: List<UiItemView>,
    val isEmpty: Boolean = postItems.isEmpty(),
)

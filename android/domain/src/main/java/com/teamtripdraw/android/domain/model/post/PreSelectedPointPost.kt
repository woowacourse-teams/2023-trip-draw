package com.teamtripdraw.android.domain.model.post

data class PreSelectedPointPost(
    val tripId: Long,
    val pointId: Long,
    val title: String,
    val writing: String,
    val address: String
)
package com.teamtripdraw.android.domain.model.post

import java.io.File

data class PrePost(
    val tripId: Long,
    val pointId: Long,
    val title: String,
    val writing: String,
    val address: String,
    val imageFile: File? = null
)

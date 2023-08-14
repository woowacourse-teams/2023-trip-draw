package com.teamtripdraw.android.domain.model.post

import java.io.File

data class PrePatchPost(
    val postId: Long,
    val title: String,
    val writing: String,
    val imageFile: File? = null,
)

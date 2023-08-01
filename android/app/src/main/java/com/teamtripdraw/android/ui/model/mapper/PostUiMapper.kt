package com.teamtripdraw.android.ui.model.mapper

import com.teamtripdraw.android.domain.model.post.Post
import com.teamtripdraw.android.ui.model.UiPostItem
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun Post.toPresentation() = UiPostItem(
    id = this.postId,
    title = this.title,
    address = this.address,
    writing = this.writing,
    recordedAt = this.point.recordedAt.formattedDateTime(),
    thumbnail = this.postImageUrl
)

private fun LocalDateTime.formattedDateTime() =
    this.format(DateTimeFormatter.ofPattern("yyyy.MM.dd | HH:mm"))

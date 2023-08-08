package com.teamtripdraw.android.ui.model.mapper

import com.teamtripdraw.android.domain.model.post.Post
import com.teamtripdraw.android.support.framework.presentation.LocalDateTimeFormatter.isoRemoveNanoSecondFormatter
import com.teamtripdraw.android.ui.model.UiPostDetail
import com.teamtripdraw.android.ui.model.UiPostItem
import java.time.LocalDateTime

fun Post.toPresentation() = UiPostItem(
    id = this.postId,
    title = this.title,
    address = this.address,
    writing = this.writing,
    recordedAt = this.point.recordedAt.formattedDateTime(),
    thumbnail = this.postImageUrl
)

fun Post.toDetailPresentation() = UiPostDetail(
    id = this.postId,
    title = this.title,
    address = this.address,
    writing = this.writing,
    recordedAt = this.point.recordedAt.formattedDateTime(),
    postImageUrl = this.postImageUrl,
    routeImageUrl = this.routeImageUrl
)

private fun LocalDateTime.formattedDateTime() =
    this.format(isoRemoveNanoSecondFormatter)

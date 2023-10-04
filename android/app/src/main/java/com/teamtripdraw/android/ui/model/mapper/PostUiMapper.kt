package com.teamtripdraw.android.ui.model.mapper

import com.teamtripdraw.android.domain.model.post.Post
import com.teamtripdraw.android.domain.model.post.PostOfAll
import com.teamtripdraw.android.support.framework.presentation.LocalDateTimeFormatter.displayDateTimeFormatter
import com.teamtripdraw.android.ui.model.UiPostDetail
import com.teamtripdraw.android.ui.model.UiPostItem
import com.teamtripdraw.android.ui.model.UiPostOfAll
import java.time.LocalDateTime

fun Post.toPresentation() = UiPostItem(
    id = this.postId,
    title = this.title,
    address = this.address,
    writing = this.writing,
    recordedAt = this.point.recordedAt.formattedDateTime(),
    thumbnail = this.postImageUrl,
)

fun Post.toDetailPresentation() = UiPostDetail(
    id = this.postId,
    title = this.title,
    address = this.address,
    writing = this.writing,
    recordedAt = this.point.recordedAt.formattedDateTime(),
    postImageUrl = this.postImageUrl,
    routeImageUrl = this.routeImageUrl,
)

fun PostOfAll.toPresentation() = UiPostOfAll(
    postId = this.postId,
    tripId = this.tripId,
    title = this.title,
    writing = this.writing,
    address = this.address,
    postImageUrl = this.postImageUrl,
    routeImageUrl = this.routeImageUrl,
    recordedAt = this.recordedAt.formattedDateTime(),

)

private fun LocalDateTime.formattedDateTime() =
    this.format(displayDateTimeFormatter)

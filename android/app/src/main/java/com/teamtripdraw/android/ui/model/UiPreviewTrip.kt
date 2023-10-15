package com.teamtripdraw.android.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UiPreviewTrip(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val routeImageUrl: String,
    val isMine: Boolean = true,
) : Parcelable

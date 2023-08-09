package com.teamtripdraw.android.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UiTrip(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val routeImageUrl: String
) : Parcelable

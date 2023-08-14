package com.teamtripdraw.android.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UiLoginInfo(
    val platform: String,
    val socialToken: String,
) : Parcelable

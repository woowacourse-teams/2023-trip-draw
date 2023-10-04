package com.teamtripdraw.android.ui.post.writing

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

enum class WritingMode {
    NEW, EDIT
}

@Parcelize
enum class ParcelableWritingMode : Parcelable {
    NEW, EDIT
}

package com.teamtripdraw.android.ui.common.bindingAdapter

import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.teamtripdraw.android.R
import com.teamtripdraw.android.domain.model.post.PostWritingValidState

@BindingAdapter("app:postWritingCompleteButtonState")
fun TextView.setDrawable(state: PostWritingValidState) {
    val btnEnabled: Boolean = when (state) {
        PostWritingValidState.AVAILABLE -> true
        PostWritingValidState.EMPTY_TITLE, PostWritingValidState.EMPTY_WRITING -> false
    }
    val textColor =
        if (btnEnabled) R.color.td_black
        else R.color.td_dark_gray

    this.isEnabled = btnEnabled
    this.setTextColor(ResourcesCompat.getColor(resources, textColor, null))
}
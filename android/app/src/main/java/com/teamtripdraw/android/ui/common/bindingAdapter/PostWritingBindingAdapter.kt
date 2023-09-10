package com.teamtripdraw.android.ui.common.bindingAdapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.teamtripdraw.android.R
import com.teamtripdraw.android.domain.model.post.PostWritingValidState

@BindingAdapter("app:postWritingCompleteButtonState")
fun TextView.setPostWritingCompleteButtonState(state: PostWritingValidState) {
    val btnEnabled: Boolean = when (state) {
        PostWritingValidState.AVAILABLE -> true
        PostWritingValidState.EMPTY_TITLE, PostWritingValidState.EMPTY_WRITING -> false
    }
    val textColor =
        if (btnEnabled) {
            R.color.td_black
        } else {
            R.color.td_dark_gray
        }

    this.isEnabled = btnEnabled
    this.setTextColor(ResourcesCompat.getColor(resources, textColor, null))
}

@BindingAdapter("app:setPostWritingImageVisibility")
fun ImageView.setPostWritingImageVisibility(isVisible: Boolean) {
    visibility =
        if (isVisible) {
            View.VISIBLE
        } else {
            View.GONE
        }
}

@BindingAdapter("app:setPostWritingImage")
fun ImageView.setPostWritingImage(imgUrl: String?) {
    if (imgUrl == null || imgUrl.isBlank()) return
    Glide.with(this.context)
        .load(imgUrl)
        .placeholder(R.drawable.shape_td_gray_fill_0_rect)
        .error(R.drawable.shape_td_gray_fill_0_rect)
        .transform(CenterCrop(), RoundedCorners(20))
        .into(this)
}

package com.teamtripdraw.android.ui.common.bindingAdapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.teamtripdraw.android.R

@BindingAdapter("app:setImage")
fun ImageView.setImage(imgUrl: String?) {
    Glide.with(this.context)
        .load(imgUrl)
        .placeholder(R.drawable.shape_td_logo_fill_10_rect)
        .error(R.drawable.shape_td_logo_fill_10_rect)
        .into(this)
}

@BindingAdapter("app:setMessageVisibility")
fun TextView.setMessageVisibility(isVisible: Boolean) {
    if (isVisible) {
        this.visibility = View.VISIBLE
    }
}

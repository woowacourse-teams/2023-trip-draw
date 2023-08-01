package com.teamtripdraw.android.ui.common.bindingAdapter

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.teamtripdraw.android.R

@BindingAdapter("app:setImage")
fun ImageView.setImage(imgUrl: String?) {
    if (imgUrl == null) {
        this.visibility = View.GONE
        return
    }

    Glide.with(this.context)
        .load(imgUrl)
        .placeholder(R.drawable.shape_td_gray_fill_0_rect)
        .error(R.drawable.shape_td_gray_fill_0_rect)
        .into(this)
}

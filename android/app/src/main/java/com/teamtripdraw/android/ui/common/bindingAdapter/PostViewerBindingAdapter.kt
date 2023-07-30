package com.teamtripdraw.android.ui.common.bindingAdapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("app:setThumbnail")
fun setImgUrl(imageView: ImageView, imgUrl: String) {
    Glide.with(imageView.context)
        .load(imgUrl)
        .apply(RequestOptions.bitmapTransform(RoundedCorners(12)))
        .into(imageView)
}

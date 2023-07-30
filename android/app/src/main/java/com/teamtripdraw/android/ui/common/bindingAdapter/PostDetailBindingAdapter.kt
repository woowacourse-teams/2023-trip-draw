package com.teamtripdraw.android.ui.common.bindingAdapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.stfalcon.imageviewer.StfalconImageViewer

@BindingAdapter("app:setImage")
fun setImage(imageView: ImageView, imgUrl: String) {
    Glide.with(imageView.context)
        .load(imgUrl)
        .into(imageView)
}

@BindingAdapter("app:onClickImage")
fun showImageViewer(imageView: ImageView, imgUrl: String) {
    imageView.setOnClickListener {
        StfalconImageViewer.Builder(imageView.context, listOf(imgUrl)) { view, image ->
            Glide.with(imageView.context)
                .load(image)
                .into(view)
        }.show()
    }
}

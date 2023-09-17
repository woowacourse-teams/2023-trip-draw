package com.teamtripdraw.android.ui.common.bindingAdapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.stfalcon.imageviewer.StfalconImageViewer
import com.teamtripdraw.android.R

@BindingAdapter("app:loadPostImage")
fun ImageView.loadPostImage(imgUrl: String?) {
    Glide.with(this.context)
        .load(imgUrl)
        .placeholder(R.drawable.shape_td_gray_fill_0_rect)
        .error(R.drawable.img_default_image)
        .into(this)
}

@BindingAdapter("app:clickToShowImageViewer")
fun ImageView.clickToShowImageViewer(imgUrl: String?) {
    this.setOnClickListener {
        StfalconImageViewer.Builder(this.context, listOf(imgUrl)) { view, image ->
            Glide.with(this.context)
                .load(image)
                .placeholder(R.drawable.shape_td_gray_fill_0_rect)
                .error(R.drawable.shape_td_gray_fill_0_rect)
                .into(view)
        }.show()
    }
}

@BindingAdapter("app:loadThumbnailRoundImage")
fun ImageView.loadThumbnailRoundImage(imgUrl: String?) {
    Glide.with(this.context)
        .load(imgUrl)
        .placeholder(R.drawable.shape_td_gray_fill_0_rect)
        .error(R.drawable.img_default_image)
        .transform(CenterCrop(), RoundedCorners(20))
        .into(this)
}

@BindingAdapter("app:loadTripThumbnailImage")
fun ImageView.loadTripThumbnailImage(imgUrl: String) {
    Glide.with(this.context)
        .load(imgUrl)
        .placeholder(R.drawable.shape_td_gray_fill_0_rect)
        .error(R.drawable.img_default_trip_thumbnail)
        .transform(CenterCrop(), RoundedCorners(20))
        .into(this)
}

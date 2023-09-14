package com.teamtripdraw.android.ui.common.bindingAdapter

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.stfalcon.imageviewer.StfalconImageViewer
import com.teamtripdraw.android.R
import java.io.File

@BindingAdapter("app:setImageVisibility")
fun ImageView.setImageVisibility(imgUrl: String?) {
    if (imgUrl.isNullOrBlank()) {
        this.visibility = View.GONE
        return
    }
    this.visibility = View.VISIBLE
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

@BindingAdapter("app:setRoundImage")
fun ImageView.setRoundImage(imgUrl: String?) {
    Glide.with(this.context)
        .load(imgUrl)
        .placeholder(R.drawable.shape_td_gray_fill_0_rect)
        .error(R.drawable.img_default_image)
        .transform(CenterCrop(), RoundedCorners(20))
        .into(this)
}

@BindingAdapter("app:setRoundImageFile")
fun ImageView.setRoundImageFile(imgFile: File?) {
    Glide.with(this.context)
        .load(imgFile)
        .placeholder(R.drawable.shape_td_gray_fill_0_rect)
        .error(R.drawable.shape_td_gray_fill_0_rect)
        .transform(CenterCrop(), RoundedCorners(20))
        .into(this)
}

@BindingAdapter("app:setFileVisibility")
fun ImageView.setFileVisibility(imgFile: File?) {
    if (imgFile == null) {
        this.visibility = View.GONE
        return
    }
    this.visibility = View.VISIBLE
}

@BindingAdapter("app:setTripThumbnailImage")
fun ImageView.setTripThumbnailImage(imgUrl: String) {
    Glide.with(this.context)
        .load(imgUrl)
        .placeholder(R.drawable.shape_td_gray_fill_0_rect)
        .error(R.drawable.img_default_trip_thumbnail)
        .transform(CenterCrop(), RoundedCorners(20))
        .into(this)
}

package com.teamtripdraw.android.ui.common.bindingAdapter

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.stfalcon.imageviewer.StfalconImageViewer
import com.teamtripdraw.android.R

@BindingAdapter("app:setImageWithVisibility")
fun ImageView.setImageWithVisibility(imgUrl: String?) {
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

@BindingAdapter("app:clickToShowImageViewer")
fun ImageView.showImageViewer(imgUrl: String?) {
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

@BindingAdapter("app:setRoundImageWithVisibility")
fun ImageView.setRoundImageWithVisibility(imgUrl: String?) {
    if (imgUrl == null) {
        this.visibility = View.GONE
        return
    }

    Glide.with(this.context)
        .load(imgUrl)
        .placeholder(R.drawable.shape_td_gray_fill_0_rect)
        .error(R.drawable.shape_td_gray_fill_0_rect)
        .apply(RequestOptions.bitmapTransform(RoundedCorners(12)))
        .into(this)
}

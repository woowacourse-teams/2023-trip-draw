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

@BindingAdapter("app:setImageWithVisibility")
fun ImageView.setImageWithVisibility(imgUrl: String?) {
    if (imgUrl == null) {
        this.visibility = View.GONE
        return
    } else {
        this.visibility = View.VISIBLE
        Glide.with(this.context)
            .load(imgUrl)
            .placeholder(R.drawable.shape_td_gray_fill_0_rect)
            .error(R.drawable.shape_td_gray_fill_0_rect)
            .into(this)
    }
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

@BindingAdapter("app:setRoundImageWithVisibility")
fun ImageView.setRoundImageWithVisibility(imgUrl: String?) {
    if (imgUrl == null) {
        this.visibility = View.GONE
        return
    } else {
        this.visibility = View.VISIBLE
        Glide.with(this.context)
            .load(imgUrl)
            .placeholder(R.drawable.shape_td_gray_fill_0_rect)
            .error(R.drawable.shape_td_gray_fill_0_rect)
            .transform(CenterCrop(), RoundedCorners(20))
            .into(this)
    }
}

@BindingAdapter("app:setRoundImageWithVisibility")
fun ImageView.setRoundImageWithVisibility(imgFile: File?) {
    if (imgFile == null) {
        this.visibility = View.GONE
        return
    } else {
        this.visibility = View.VISIBLE
        Glide.with(this.context)
            .load(imgFile)
            .placeholder(R.drawable.shape_td_gray_fill_0_rect)
            .error(R.drawable.shape_td_gray_fill_0_rect)
            .transform(CenterCrop(), RoundedCorners(20))
            .into(this)
    }
}

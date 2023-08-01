package com.teamtripdraw.android.ui.common.bindingAdapter

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.stfalcon.imageviewer.StfalconImageViewer
import com.teamtripdraw.android.R

@BindingAdapter("app:onClickImage")
fun ImageView.showImageViewer(imgUrl: String?) {
    if (imgUrl == null) {
        this.visibility = View.GONE
        return
    }
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

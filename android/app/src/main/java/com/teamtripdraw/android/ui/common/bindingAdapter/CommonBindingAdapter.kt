package com.teamtripdraw.android.ui.common.bindingAdapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("app:setNoItemsMessageVisibility")
fun TextView.setNoItemsMessageVisibility(isVisible: Boolean) {
    if (isVisible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

@BindingAdapter("app:setImageVisibility")
fun ImageView.setImageVisibility(imgUrl: String?) {
    if (imgUrl.isNullOrBlank()) {
        this.visibility = View.GONE
        return
    }
    this.visibility = View.VISIBLE
}

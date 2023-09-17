package com.teamtripdraw.android.ui.common.bindingAdapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("app:setMessageVisibility")
fun TextView.setMessageVisibility(isVisible: Boolean) {
    if (isVisible) {
        this.visibility = View.VISIBLE
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

package com.teamtripdraw.android.ui.common.bindingAdapter

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("app:setEditDeleteIconVisibility")
fun ImageView.setIconVisibility(isMine: Boolean?) {
    if (isMine == null) return
    if (isMine) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

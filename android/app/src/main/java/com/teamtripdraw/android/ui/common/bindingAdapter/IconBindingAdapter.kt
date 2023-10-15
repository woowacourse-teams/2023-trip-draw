package com.teamtripdraw.android.ui.common.bindingAdapter

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("app:setIconVisibility")
fun View.setIconVisibility(isMine: Boolean?) {
    if (isMine == null) return
    if (isMine) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

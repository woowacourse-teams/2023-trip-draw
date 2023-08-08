package com.teamtripdraw.android.ui.common.bindingAdapter

import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.teamtripdraw.android.R
import com.teamtripdraw.android.domain.model.trip.TripTitleValidState

@BindingAdapter("app:setDialogEditTextDrawable")
fun EditText.setDialogEditTextDrawable(state: TripTitleValidState) {
    val etLineColor = when (state) {
        TripTitleValidState.DEFAULT -> R.drawable.shape_td_main_blue_line_5_rect
        TripTitleValidState.EXCEED_LIMIT, TripTitleValidState.CONTAIN_BLANK -> R.drawable.shape_td_red_line_5_rect
    }

    this.background = ResourcesCompat.getDrawable(resources, etLineColor, null)
}

@BindingAdapter("app:setDialogWarningMessage")
fun TextView.setDialogWarningMessage(state: TripTitleValidState) {
    when (state) {
        TripTitleValidState.DEFAULT -> {
            this.visibility = View.INVISIBLE
        }
        TripTitleValidState.EXCEED_LIMIT -> {
            this.visibility = View.VISIBLE
            this.text = resources.getString(R.string.input_dialog_warning_exceed_limit)
        }
        TripTitleValidState.CONTAIN_BLANK -> {
            this.visibility = View.VISIBLE
            this.text = resources.getString(R.string.tv_dialog_input_title)
        }
    }
}

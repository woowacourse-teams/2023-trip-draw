package com.teamtripdraw.android.ui.common.bindingAdapter

import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.teamtripdraw.android.R
import com.teamtripdraw.android.domain.model.trip.TripTitleValidState

@BindingAdapter("app:setDialogButtonEnabled")
fun AppCompatButton.setDialogButtonEnabled(state: TripTitleValidState) {
    val btnEnabled: Boolean = when (state) {
        TripTitleValidState.AVAILABLE -> true
        TripTitleValidState.EXCEED_LIMIT, TripTitleValidState.CONTAIN_BLANK -> false
    }
    val btnBackground =
        if (btnEnabled) {
            R.drawable.shape_td_main_blue_fill_5_rect
        } else {
            R.drawable.shape_td_gray_fill_5_rect
        }

    this.isEnabled = btnEnabled
    this.background = ResourcesCompat.getDrawable(resources, btnBackground, null)
}

@BindingAdapter("app:setDialogEditTextBackground")
fun EditText.setDialogEditTextBackground(state: TripTitleValidState) {
    val etLineColor = when (state) {
        TripTitleValidState.AVAILABLE -> R.drawable.shape_td_main_blue_line_5_rect
        TripTitleValidState.EXCEED_LIMIT, TripTitleValidState.CONTAIN_BLANK -> R.drawable.shape_td_red_line_5_rect
    }

    this.background = ResourcesCompat.getDrawable(resources, etLineColor, null)
}

@BindingAdapter("app:setDialogWarningMessage")
fun TextView.setDialogWarningMessage(state: TripTitleValidState) {
    when (state) {
        TripTitleValidState.AVAILABLE -> {
            this.visibility = View.INVISIBLE
        }
        TripTitleValidState.EXCEED_LIMIT -> {
            this.visibility = View.VISIBLE
            this.text = resources.getString(R.string.warning_exceed_limit_input_dialog)
        }
        TripTitleValidState.CONTAIN_BLANK -> {
            this.visibility = View.VISIBLE
            this.text = resources.getString(R.string.tv_dialog_input_title)
        }
    }
}

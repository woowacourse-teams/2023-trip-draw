package com.teamtripdraw.android.ui.common.bindingAdapter

import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.teamtripdraw.android.R
import com.teamtripdraw.android.domain.user.NicknameValidState

@BindingAdapter("nicknameCompleteButtonState")
fun AppCompatButton.setDrawable(state: NicknameValidState) {
    val btnEnabled: Boolean = when (state) {
        NicknameValidState.AVAILABLE -> true
        NicknameValidState.EMPTY, NicknameValidState.EXCEED_LIMIT, NicknameValidState.CONTAIN_BLANK, NicknameValidState.DUPLICATE -> false
    }
    val btnBackground =
        if (btnEnabled) R.drawable.shape_td_sub_blue_fill_12_rect
        else R.drawable.shape_td_gray_fill_12_rect

    this.isEnabled = btnEnabled
    this.background = ResourcesCompat.getDrawable(resources, btnBackground, null)
}

@BindingAdapter("nicknameEditTextState")
fun EditText.setDrawable(state: NicknameValidState) {
    val etLineColor = when (state) {
        NicknameValidState.EMPTY, NicknameValidState.AVAILABLE, NicknameValidState.DUPLICATE -> R.drawable.shape_td_dark_gray_line_12_rect
        NicknameValidState.EXCEED_LIMIT, NicknameValidState.CONTAIN_BLANK -> R.drawable.shape_td_red_line_12_rect
    }

    this.background = ResourcesCompat.getDrawable(resources, etLineColor, null)
}

@BindingAdapter("nicknameValidStateTextView")
fun TextView.setDrawable(state: NicknameValidState) {
    when (state) {
        NicknameValidState.EMPTY, NicknameValidState.AVAILABLE, NicknameValidState.DUPLICATE -> {
            this.visibility = View.GONE
        }
        NicknameValidState.EXCEED_LIMIT -> {
            this.visibility = View.VISIBLE
            this.text = resources.getString(R.string.warning_exceed_limit)
        }
        NicknameValidState.CONTAIN_BLANK -> {
            this.visibility = View.VISIBLE
            this.text = resources.getString(R.string.warning_contain_blank)
        }
    }
}

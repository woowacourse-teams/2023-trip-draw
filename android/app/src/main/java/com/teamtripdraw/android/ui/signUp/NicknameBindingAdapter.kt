package com.teamtripdraw.android.ui.signUp

import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.teamtripdraw.android.R

@BindingAdapter("nicknameCompleteBtnStatus")
fun AppCompatButton.setDrawable(state: NicknameState) {
    this.background = ResourcesCompat.getDrawable(resources, state.completeBtnDrawable, null)
    this.isEnabled = state.completeBtnEnabled
}

@BindingAdapter("nicknameEtStatus")
fun EditText.setDrawable(state: NicknameState) {
    if (state.warningState) {
        this.background =
            ResourcesCompat.getDrawable(resources, R.drawable.shape_td_red_line_12_rect, null)
    } else {
        this.background =
            ResourcesCompat.getDrawable(resources, R.drawable.shape_td_dark_gray_line_12_rect, null)
    }
}

@BindingAdapter("nicknameTvStatus")
fun TextView.setDrawable(state: NicknameState) {
    if (state.warningState) {
        this.setTextColor(ResourcesCompat.getColor(resources, R.color.td_red, null))
        this.visibility = View.VISIBLE
        this.text = resources.getString(state.warningMessage!!)
    } else {
        this.visibility = View.GONE
    }
}

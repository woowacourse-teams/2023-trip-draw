package com.teamtripdraw.android.ui.common.bindingAdapter

import android.content.res.ColorStateList
import android.view.View
import androidx.databinding.BindingAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.teamtripdraw.android.R
import com.teamtripdraw.android.ui.home.HomeUiState
import com.teamtripdraw.android.ui.home.HomeUiState.BEFORE_TRIP
import com.teamtripdraw.android.ui.home.HomeUiState.ON_TRIP

@BindingAdapter("app:homeUiStateBeforeTrip")
fun View.setHomeUiStateBeforeTrip(homeUiState: HomeUiState) {
    when (homeUiState) {
        BEFORE_TRIP -> {
            this.visibility = View.VISIBLE
        }
        ON_TRIP -> {
            this.visibility = View.GONE
        }
    }
}

@BindingAdapter("app:homeUiStateOnTrip")
fun View.setHomeUiStateOnTrip(homeUiState: HomeUiState) {
    when (homeUiState) {
        ON_TRIP -> {
            this.visibility = View.VISIBLE
        }
        BEFORE_TRIP -> {
            this.visibility = View.GONE
        }
    }
}

@BindingAdapter("app:homeUiStateOnTripForFabText")
fun View.setHomeUiStateOnTripForFabText(homeUiState: HomeUiState) {
    when (homeUiState) {
        ON_TRIP -> {}
        BEFORE_TRIP -> {
            this.visibility = View.GONE
        }
    }
}

@BindingAdapter("app:setFloatingActionButtonTint")
fun FloatingActionButton.setFloatingActionButtonTint(fabState: Boolean) {
    if (fabState) {
        this.imageTintList = ColorStateList.valueOf(this.context.getColor(R.color.td_main_blue))
        this.backgroundTintList = ColorStateList.valueOf(this.context.getColor(R.color.td_white))
    } else {
        this.imageTintList = ColorStateList.valueOf(this.context.getColor(R.color.td_white))
        this.backgroundTintList =
            ColorStateList.valueOf(this.context.getColor(R.color.td_main_blue))
    }
}

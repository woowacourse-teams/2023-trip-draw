package com.teamtripdraw.android.ui.common.bindingAdapter

import android.view.View
import androidx.databinding.BindingAdapter
import com.teamtripdraw.android.ui.home.HomeUiState
import com.teamtripdraw.android.ui.home.HomeUiState.BEFORE_TRIP
import com.teamtripdraw.android.ui.home.HomeUiState.EDIT_TRIP
import com.teamtripdraw.android.ui.home.HomeUiState.ON_TRIP

@BindingAdapter("app:homeUiStateBeforeTrip")
fun View.setHomeUiStateBeforeTrip(homeUiState: HomeUiState) {
    when (homeUiState) {
        BEFORE_TRIP -> {
            this.visibility = View.VISIBLE
        }
        ON_TRIP, EDIT_TRIP -> {
            this.visibility = View.GONE
        }
    }
}

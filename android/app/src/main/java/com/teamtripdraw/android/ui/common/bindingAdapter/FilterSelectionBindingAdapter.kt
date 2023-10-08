package com.teamtripdraw.android.ui.common.bindingAdapter

import android.view.View
import android.widget.NumberPicker
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.BindingAdapter
import com.teamtripdraw.android.domain.model.filterOption.FilterOption
import com.teamtripdraw.android.ui.filter.FilterOptionsView
import com.teamtripdraw.android.ui.filter.FilterType

@BindingAdapter("app:setupFilterOptionTitle", "app:setupFilterOptions")
fun FilterOptionsView.setupFilterOption(title: String, options: List<FilterOption>) {
    setTitle(title)
    setupOptions(options)
}

@BindingAdapter("app:setupFilterOptionHour")
fun NumberPicker.setupFilterOptionHour(options: List<FilterOption>) {
    minValue = options.first().value as Int
    maxValue = options.last().value as Int
}

@BindingAdapter("app:setupFilterOptionInitHour")
fun NumberPicker.setupFilterOptionInitHour(options: List<FilterOption>) {
    value = options.last().value as Int
}

@BindingAdapter("app:setVisibilityIfTrip")
fun View.setVisibilityIfTrip(filterType: FilterType?) {
    if (filterType == FilterType.TRIP) {
        visibility = View.GONE
    }
}

@BindingAdapter("app:setAddressText")
fun AppCompatButton.setAddressText(address: String?) {
    if (address == null) return
    if (address.isEmpty()) {
        this.text = INITIAL_ADDRESS_BTN_TEXT
    } else {
        this.text = address
    }
}

private const val INITIAL_ADDRESS_BTN_TEXT = "+"

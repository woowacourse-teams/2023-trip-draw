package com.teamtripdraw.android.ui.common.bindingAdapter

import android.view.View
import android.widget.NumberPicker
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.BindingAdapter
import com.teamtripdraw.android.domain.model.filterOption.FilterOption
import com.teamtripdraw.android.ui.filter.FilterOptionsView
import com.teamtripdraw.android.ui.filter.FilterType

@BindingAdapter("app:setupFilterOptionTitle", "app:setupFilterOptions", "app:setupSelectedOptions")
fun FilterOptionsView.setupFilterOption(
    title: String,
    options: List<FilterOption>,
    selectedOptions: List<Int>? = null,
) {
    setTitle(title)
    setupOptions(options, selectedOptions)
}

@BindingAdapter("app:setupFilterOptionHour")
fun NumberPicker.setupFilterOptionHour(options: List<FilterOption>) {
    minValue = options.first().value as Int
    maxValue = options.last().value as Int
}

@BindingAdapter("app:setupFilterOptionHourFrom")
fun NumberPicker.setupFilterOptionHourFrom(selectedOptions: List<Int>?) {
    if (selectedOptions == null) return
    value = selectedOptions.first()
}

@BindingAdapter("app:setupFilterOptionHourTo")
fun NumberPicker.setupFilterOptionHourTo(selectedOptions: List<Int>?) {
    value = maxValue

    if (selectedOptions == null) return
    value = selectedOptions.last()
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

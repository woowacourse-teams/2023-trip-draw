package com.teamtripdraw.android.ui.common.bindingAdapter

import android.view.View
import android.widget.NumberPicker
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

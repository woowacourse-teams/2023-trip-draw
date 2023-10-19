package com.teamtripdraw.android.ui.common.bindingAdapter

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.teamtripdraw.android.R
import com.teamtripdraw.android.domain.model.filterOption.FilterOption
import com.teamtripdraw.android.ui.filter.FilterOptionsView
import com.teamtripdraw.android.ui.filter.FilterType
import com.teamtripdraw.android.ui.model.UiAddressSelectionItem

@BindingAdapter("app:setupFilterOptions", "app:setupSelectedOptions")
fun <T> FilterOptionsView.setupFilterOption(
    options: List<FilterOption<T>>,
    selectedOptions: List<Int>? = null,
) {
    setOptions(options, selectedOptions)
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

@BindingAdapter("app:setAddressSelectionItemBackground")
fun TextView.testSetColor(uiAddressSelectionItem: UiAddressSelectionItem) {
    val colorResId: Int =
        if (uiAddressSelectionItem.isSelected) {
            R.color.td_light_blue
        } else {
            R.color.td_white
        }

    setBackgroundColor(
        ContextCompat.getColor(this.context, colorResId),
    )
}

private const val INITIAL_ADDRESS_BTN_TEXT = "+"

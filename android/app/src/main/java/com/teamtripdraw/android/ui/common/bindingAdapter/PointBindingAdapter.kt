package com.teamtripdraw.android.ui.common.bindingAdapter

import android.location.Geocoder
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.teamtripdraw.android.support.framework.presentation.extensions.getAdministrativeArea
import java.util.*

@BindingAdapter("app:administrativeAreaText")
fun TextView.setText(latitude: Double, longitude: Double) {
    val geocoder = Geocoder(context, Locale.KOREAN)
    text = geocoder.getAdministrativeArea(latitude, longitude)
}
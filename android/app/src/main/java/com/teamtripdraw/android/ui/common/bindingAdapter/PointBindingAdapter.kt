package com.teamtripdraw.android.ui.common.bindingAdapter

import android.location.Geocoder
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.teamtripdraw.android.support.framework.presentation.extensions.getAdministrativeAddress
import java.util.Locale

@BindingAdapter("app:administrativeAreaText")
fun TextView.setAdministrativeAddress(latitude: Double, longitude: Double) {
    val geocoder = Geocoder(context, Locale.KOREAN)
    text = geocoder.getAdministrativeAddress(latitude, longitude)
}
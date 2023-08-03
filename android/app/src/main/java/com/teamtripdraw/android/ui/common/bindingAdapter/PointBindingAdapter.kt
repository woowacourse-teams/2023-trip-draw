package com.teamtripdraw.android.ui.common.bindingAdapter

import android.location.Geocoder
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.teamtripdraw.android.domain.model.point.LatLngPoint
import com.teamtripdraw.android.support.framework.presentation.extensions.getAdministrativeAddress
import java.util.Locale

@BindingAdapter("app:administrativeAreaText")
fun TextView.setAdministrativeAddress(latLngPoint: LatLngPoint?) {
    if (latLngPoint == null) return
    val geocoder = Geocoder(context, Locale.KOREAN)
    text = geocoder.getAdministrativeAddress(latLngPoint.latitude, latLngPoint.longitude)
}
package com.teamtripdraw.android.ui.common.bindingAdapter.home

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.teamtripdraw.android.ui.model.UiPoint

@BindingAdapter("app:setMarkerSelectedBottomSheetRecordTime")
fun TextView.setMarkerSelectedBottomSheetRecordTime(uiPoint: UiPoint?) {
    if (uiPoint == null) return
    text = uiPoint.recordedAt
}

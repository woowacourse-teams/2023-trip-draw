package com.teamtripdraw.android.ui.model.mapper

import com.naver.maps.geometry.LatLng

data class UiMarkerInfo(
    val latLng: LatLng,
    val hasPost: Boolean
)

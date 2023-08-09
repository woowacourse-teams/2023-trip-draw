package com.teamtripdraw.android.ui.model

import com.naver.maps.geometry.LatLng

data class UiMarkerInfo(
    val pointId: Long,
    val latLng: LatLng,
    val hasPost: Boolean
)

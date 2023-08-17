package com.teamtripdraw.android.ui.model

import com.naver.maps.geometry.LatLng

data class UiRoute(
    val value: List<UiPoint>,
    val enablePolyLine: Boolean
) {
    fun getLatLngs(): List<LatLng> =
        value.map {
            LatLng(it.latitude, it.longitude)
        }

    fun getUiMarkerInfo(): List<UiMarkerInfo> =
        value.map {
            UiMarkerInfo(
                it.pointId,
                LatLng(it.latitude, it.longitude),
                it.hasPost
            )
        }
}

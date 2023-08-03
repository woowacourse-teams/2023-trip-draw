package com.teamtripdraw.android.ui.model

import com.naver.maps.geometry.LatLng
import com.teamtripdraw.android.ui.model.mapper.UiMarkerInfo

data class UiRoute(
    val value: List<UiPoint>
) {
    fun getLatLngs(): List<LatLng> =
        value.map {
            LatLng(it.latitude, it.longitude)
        }

    fun getUiMarkerInfo(): List<UiMarkerInfo> =
        value.map {
            UiMarkerInfo(
                LatLng(it.latitude, it.longitude),
                it.hasPost
            )
        }
}

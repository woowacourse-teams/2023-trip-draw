package com.teamtripdraw.android.ui.common.bindingAdapter

import android.content.Context
import android.graphics.PointF
import android.view.View
import androidx.databinding.BindingAdapter
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PolylineOverlay
import com.teamtripdraw.android.R
import com.teamtripdraw.android.support.framework.presentation.resolution.toDP
import com.teamtripdraw.android.ui.model.UiRoute
import com.teamtripdraw.android.ui.model.mapper.UiMarkerInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@BindingAdapter("app:setPolyLineNaverMap", "app:setPolyLineUiRoute")
fun View.setPolyLine(naverMap: NaverMap?, uiRoute: UiRoute?) {
    if (uiRoute == null) return
    if (!uiRoute.checkAvailablePolyLine()) return
    if (naverMap == null) return
    initializePolyLine()
    updatePolyLine(uiRoute, naverMap)
}

private fun View.updatePolyLine(
    uiRoute: UiRoute,
    naverMap: NaverMap
) {
    val polyLine = getTag(R.id.NAVER_MAP_POLY_LINE_TAG) as PolylineOverlay
    polyLine.coords = uiRoute.getLatLngs()
    polyLine.map = naverMap
}

private fun View.initializePolyLine() {
    if (this.getTag(R.id.NAVER_MAP_POLY_LINE_TAG) == null) setTag(
        R.id.NAVER_MAP_POLY_LINE_TAG,
        setPolyLineSetting(PolylineOverlay(), context)
    )
}

private fun setPolyLineSetting(polyLine: PolylineOverlay, context: Context): PolylineOverlay =
    polyLine.apply {
        width = toDP(context, POLY_LINE_WIDTH)
        color = context.getColor(R.color.td_main_blue)
        capType = PolylineOverlay.LineCap.Round
        joinType = PolylineOverlay.LineJoin.Round
    }

private const val POLY_LINE_WIDTH = 30

@BindingAdapter("app:setMarkerNaverMap", "app:setMarkerUiRoute")
fun View.setMarker(naverMap: NaverMap?, uiRoute: UiRoute?) {
    if (uiRoute == null) return
    if (naverMap == null) return
    removeOldMarker()
    initializeMarker(naverMap, uiRoute)
}

private fun View.removeOldMarker() {
    if (getTag(R.id.NAVER_MAP_MARKERS_TAG) != null) {
        val oldMarkers = getTag(R.id.NAVER_MAP_MARKERS_TAG) as List<Marker>
        oldMarkers.forEach { it.map = null }
    }
}

private fun View.initializeMarker(naverMap: NaverMap, uiRoute: UiRoute) {
    CoroutineScope(Dispatchers.Default).launch {
        val deferredNewMarkers = async {
            uiRoute.getUiMarkerInfo().map { UiMarkerInfo ->
                setMarkerSetting(UiMarkerInfo)
            }
        }
        val newMarkers = deferredNewMarkers.await()
        setTag(R.id.NAVER_MAP_MARKERS_TAG, newMarkers)
        withContext(Dispatchers.Main) {
            newMarkers.forEach { marker: Marker ->
                marker.map = naverMap
            }
        }
    }
}

private fun setMarkerSetting(UiMarkerInfo: UiMarkerInfo): Marker =
    Marker().apply {
        this.position = UiMarkerInfo.latLng
        this.anchor = PointF(ANCHOR_X_LOCATION_VALUE, ANCHOR_Y_LOCATION_VALUE)
        when (UiMarkerInfo.hasPost) {
            true -> {
                this.icon = markerHasPostImage
                this.zIndex = MARKER_HAS_POST_PRIORITY_DEGREE
            }
            false -> {
                this.icon = markerNoPostImage
            }
        }
        this.isVisible = false
    }

private val markerNoPostImage = OverlayImage.fromResource(R.drawable.ic_marker_no_post)
private val markerHasPostImage = OverlayImage.fromResource(R.drawable.ic_marker_has_post)
private val markerSelectedImage = OverlayImage.fromResource(R.drawable.ic_marker_selected)

private const val MARKER_HAS_POST_PRIORITY_DEGREE = 300
private const val ANCHOR_X_LOCATION_VALUE = 0.5f
private const val ANCHOR_Y_LOCATION_VALUE = 0.5f

@BindingAdapter("app:markerViewModeState")
fun View.toggleMarkerMode(markerViewModeState: Boolean) {
    if (getTag(R.id.NAVER_MAP_MARKERS_TAG) != null) {
        val markers = getTag(R.id.NAVER_MAP_MARKERS_TAG) as List<Marker>
        markers.forEach { it.isVisible = markerViewModeState }
    }
}

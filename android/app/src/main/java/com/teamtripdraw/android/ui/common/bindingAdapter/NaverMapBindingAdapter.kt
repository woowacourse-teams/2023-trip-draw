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
import com.teamtripdraw.android.ui.model.UiMarkerInfo
import com.teamtripdraw.android.ui.model.UiRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@BindingAdapter("app:setPolyLineNaverMap", "app:setPolyLineUiRoute")
fun View.setPolyLine(naverMap: NaverMap?, uiRoute: UiRoute?) {
    if (uiRoute == null) return
    if (!uiRoute.enablePolyLine) return
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

@BindingAdapter(
    "app:setMarkerNaverMap",
    "app:setMarkerUiRoute",
    "app:setMarkerViewModeState",
    "app:setMarkerSelectMarkerListener"
)
fun View.setMarker(
    naverMap: NaverMap?,
    uiRoute: UiRoute?,
    markerViewModeState: Boolean,
    selectMarkerListener: (Long) -> Unit
) {
    if (uiRoute == null) return
    if (naverMap == null) return
    removeOldMarker()
    initializeMarker(naverMap, uiRoute, markerViewModeState, selectMarkerListener)
}

private fun View.removeOldMarker() {
    if (getTag(R.id.NAVER_MAP_MARKERS_TAG) != null) {
        val oldMarkers = getTag(R.id.NAVER_MAP_MARKERS_TAG) as List<Marker>
        oldMarkers.forEach { it.map = null }
    }
}

private fun View.initializeMarker(
    naverMap: NaverMap,
    uiRoute: UiRoute,
    markerViewModeState: Boolean,
    selectMarkerListener: (Long) -> Unit
) {
    CoroutineScope(Dispatchers.Default).launch {
        val deferredNewMarkers = async {
            uiRoute.getUiMarkerInfo().mapIndexed() { index, UiMarkerInfo ->
                setMarkerSetting(
                    UiMarkerInfo,
                    index == START_MARKER_INDEX,
                    markerViewModeState,
                    selectMarkerListener
                )
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

private fun setMarkerSetting(
    uiMarkerInfo: UiMarkerInfo,
    isStartPoint: Boolean,
    markerViewModeState: Boolean,
    selectMarkerListener: (Long) -> Unit
): Marker =
    Marker().apply {
        this.position = uiMarkerInfo.latLng
        setMarkerAnchor(this, isStartPoint)
        selectMarkerIcon(this, isStartPoint, uiMarkerInfo)
        if (!isStartPoint) this.isVisible = markerViewModeState
        initMarkerClickListener(this, uiMarkerInfo.pointId, selectMarkerListener)
    }

private fun setMarkerAnchor(marker: Marker, isStartPoint: Boolean) {
    if (isStartPoint) {
        marker.anchor = PointF(
            START_MARKER_ANCHOR_X_LOCATION_VALUE,
            START_MARKER_ANCHOR_Y_LOCATION_VALUE
        )
    } else {
        marker.anchor = PointF(COMMON_ANCHOR_X_LOCATION_VALUE, COMMON_ANCHOR_Y_LOCATION_VALUE)
    }
}

private fun selectMarkerIcon(
    marker: Marker,
    isStartPoint: Boolean,
    UiMarkerInfo: UiMarkerInfo
) {
    if (isStartPoint) {
        marker.icon = markerFirstPoint
        marker.zIndex = MARKER_START_POINT_PRIORITY_DEGREE
    } else {
        selectMarkerWithPostHoldingStatus(UiMarkerInfo, marker)
    }
}

private fun selectMarkerWithPostHoldingStatus(
    UiMarkerInfo: UiMarkerInfo,
    marker: Marker
) {
    when (UiMarkerInfo.hasPost) {
        true -> {
            marker.icon = markerHasPostImage
            marker.zIndex = MARKER_HAS_POST_PRIORITY_DEGREE
        }
        false -> {
            marker.icon = markerNoPostImage
        }
    }
}

private fun initMarkerClickListener(
    marker: Marker,
    pointId: Long,
    selectMarkerListener: (Long) -> Unit
) {
    marker.setOnClickListener {
        marker.icon = markerSelectedImage
        selectMarkerListener(pointId)
        true
    }
}

private val markerFirstPoint = OverlayImage.fromResource(R.drawable.ic_marker_first_point)
private val markerNoPostImage = OverlayImage.fromResource(R.drawable.ic_marker_no_post)
private val markerHasPostImage = OverlayImage.fromResource(R.drawable.ic_marker_has_post)
private val markerSelectedImage = OverlayImage.fromResource(R.drawable.ic_marker_selected)

private const val MARKER_START_POINT_PRIORITY_DEGREE = 600
private const val MARKER_HAS_POST_PRIORITY_DEGREE = 300
private const val COMMON_ANCHOR_X_LOCATION_VALUE = 0.5f
private const val COMMON_ANCHOR_Y_LOCATION_VALUE = 0.5f
private const val START_MARKER_ANCHOR_X_LOCATION_VALUE = 0f
private const val START_MARKER_ANCHOR_Y_LOCATION_VALUE = 1f
private const val START_MARKER_INDEX = 0

@BindingAdapter("app:markerViewModeState")
fun View.toggleMarkerMode(markerViewModeState: Boolean) {
    if (getTag(R.id.NAVER_MAP_MARKERS_TAG) != null) {
        val markers = getTag(R.id.NAVER_MAP_MARKERS_TAG) as List<Marker>
        markers.forEachIndexed { index, marker ->
            if (index != START_MARKER_INDEX) marker.isVisible = markerViewModeState
        }
    }
}

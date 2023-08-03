package com.teamtripdraw.android.ui.common.bindingAdapter

import android.content.Context
import android.view.View
import androidx.databinding.BindingAdapter
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.PolylineOverlay
import com.teamtripdraw.android.R
import com.teamtripdraw.android.support.framework.presentation.resolution.toDP
import com.teamtripdraw.android.ui.model.UiRoute

@BindingAdapter("app:setPolyLineNaverMap", "app:setPolyLineUiRoute")
fun View.setPolyLine(naverMap: NaverMap?, uiRoute: UiRoute?) {
    initializePolyLine()
    updatePolyLine(uiRoute, naverMap)
}

private fun View.updatePolyLine(
    uiRoute: UiRoute?,
    naverMap: NaverMap?
) {
    if (uiRoute == null) return
    if (naverMap == null) return
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
        width = toDP(context, 30)
        color = context.getColor(R.color.td_main_blue)
        capType = PolylineOverlay.LineCap.Round
        joinType = PolylineOverlay.LineJoin.Round
    }

package com.teamtripdraw.android.support.framework.presentation.naverMap

import android.content.Context
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.util.FusedLocationSource
import com.teamtripdraw.android.support.framework.presentation.permission.LocationPermissionState.ALL_PERMISSION
import com.teamtripdraw.android.support.framework.presentation.permission.LocationPermissionState.HAS_COARSE
import com.teamtripdraw.android.support.framework.presentation.permission.LocationPermissionState.NO_PERMISSION
import com.teamtripdraw.android.support.framework.presentation.permission.getCurrentLocationPermissionState

const val LOCATION_PERMISSION_REQUEST_CODE = 1000

fun NaverMap.initUserInterface() {
    uiSettings.run {
        isLocationButtonEnabled = true
        isRotateGesturesEnabled = false
        isScaleBarEnabled = true
        isTiltGesturesEnabled = false
    }
}

fun NaverMap.initUserLocationOption(fusedLocationSource: FusedLocationSource, context: Context) {
    locationSource = fusedLocationSource
    when (getCurrentLocationPermissionState(context)) {
        HAS_COARSE, ALL_PERMISSION -> {
            uiSettings.isLocationButtonEnabled = true
            locationTrackingMode = LocationTrackingMode.Follow
        }
        NO_PERMISSION -> {}
    }
}

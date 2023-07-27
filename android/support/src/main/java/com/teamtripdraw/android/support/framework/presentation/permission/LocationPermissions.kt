package com.teamtripdraw.android.support.framework.presentation.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import com.teamtripdraw.android.support.framework.presentation.permission.LocationPermissionState.ALL_PERMISSION
import com.teamtripdraw.android.support.framework.presentation.permission.LocationPermissionState.HAS_COARSE
import com.teamtripdraw.android.support.framework.presentation.permission.LocationPermissionState.NO_PERMISSION

private const val COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
private const val FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION

fun checkForeGroundPermission(
    context: Context,
    activityResultLauncher: ActivityResultLauncher<Array<String>>
) {
    val coarseLocationState =
        ContextCompat.checkSelfPermission(context, COARSE_LOCATION) == PERMISSION_GRANTED
    val fineLocationState =
        ContextCompat.checkSelfPermission(context, FINE_LOCATION) == PERMISSION_GRANTED
    processLocationPermission(
        coarseLocationState,
        fineLocationState,
        activityResultLauncher
    )
}

private fun processLocationPermission(
    coarseLocationState: Boolean,
    fineLocationState: Boolean,
    activityResultLauncher: ActivityResultLauncher<Array<String>>
) {
    when (LocationPermissionState.get(coarseLocationState, fineLocationState)) {
        NO_PERMISSION -> {
            activityResultLauncher.launch(arrayOf(FINE_LOCATION, COARSE_LOCATION))
        }
        HAS_COARSE -> {
            activityResultLauncher.launch(arrayOf(FINE_LOCATION))
        }
        ALL_PERMISSION -> {}
    }
}

fun getCurrentLocationPermissionState(context: Context): LocationPermissionState {
    val coarseLocationState =
        ContextCompat.checkSelfPermission(context, COARSE_LOCATION) == PERMISSION_GRANTED
    val fineLocationState =
        ContextCompat.checkSelfPermission(context, FINE_LOCATION) == PERMISSION_GRANTED
    return LocationPermissionState.get(coarseLocationState, fineLocationState)
}

enum class LocationPermissionState {
    NO_PERMISSION, HAS_COARSE, ALL_PERMISSION;

    companion object {
        private const val WRONG_LOCATION_PERMISSION_STATE_ERROR = "위치 정보 권한 상태가 올바르지 않습니다."

        fun get(coarseLocationState: Boolean, fineLocationState: Boolean): LocationPermissionState {
            return when {
                !coarseLocationState && !fineLocationState -> NO_PERMISSION
                coarseLocationState && !fineLocationState -> HAS_COARSE
                coarseLocationState && fineLocationState -> ALL_PERMISSION
                else -> throw IllegalStateException(WRONG_LOCATION_PERMISSION_STATE_ERROR)
            }
        }
    }
}

package com.teamtripdraw.android.support.framework.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority

object Locations {
    private const val LOCATION_REQUEST_INTERVAL = 100L
    private const val LOCATION_REQUEST_FASTEST_INTERVAL = 10L

    fun getUpdateLocation(
        fusedLocationClient: FusedLocationProviderClient,
        context: Context,
        locationListener: (LocationResult) -> Unit,
    ) {
        if (checkLocationPermission(context)) return
        fusedLocationClient.requestLocationUpdates(
            getLocationRequest(),
            locationCallback(fusedLocationClient, locationListener),
            Looper.getMainLooper(),
        )
    }

    private fun checkLocationPermission(context: Context) =
        ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ) != PackageManager.PERMISSION_GRANTED

    private fun getLocationRequest() =
        LocationRequest.create().apply {
            interval = LOCATION_REQUEST_INTERVAL
            fastestInterval = LOCATION_REQUEST_FASTEST_INTERVAL
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }

    private fun locationCallback(
        fusedLocationClient: FusedLocationProviderClient,
        locationListener: (LocationResult) -> Unit,
    ) =
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationListener(locationResult)
                finishUpdateLocation(fusedLocationClient, this)
            }
        }

    private fun finishUpdateLocation(
        fusedLocationClient: FusedLocationProviderClient,
        locationCallback: LocationCallback,
    ) {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}

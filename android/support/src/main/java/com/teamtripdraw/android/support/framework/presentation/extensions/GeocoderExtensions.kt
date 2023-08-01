package com.teamtripdraw.android.support.framework.presentation.extensions

import android.location.Address
import android.location.Geocoder
import android.os.Build

fun Geocoder.getAdministrativeArea(latitude: Double, longitude: Double): String {
    var administrativeArea: String = "x: $latitude, y: $longitude"

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // API 레벨 33 이상
        getFromLocation(latitude, longitude, 1) { addresses: List<Address> ->
            if (addresses.isNotEmpty()
                && addresses[0].getAddressLine(0).isNotEmpty()
            ) {
                val address = addresses[0]
                administrativeArea = address.adminArea + address.subAdminArea
            }
        }
    } else { // API 레벨 33 미만
        val addresses: List<Address>? = getFromLocation(latitude, longitude, 1)
        if (addresses != null
            && addresses.isNotEmpty()
            && addresses[0].getAddressLine(0).isNotEmpty()
        ) {
            val address = addresses[0]
            administrativeArea = address.adminArea + address.subAdminArea
        }
    }

    return administrativeArea
}

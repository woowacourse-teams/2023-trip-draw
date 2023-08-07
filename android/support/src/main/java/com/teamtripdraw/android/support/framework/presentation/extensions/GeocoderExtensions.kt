package com.teamtripdraw.android.support.framework.presentation.extensions

import android.location.Address
import android.location.Geocoder
import android.os.Build

fun Geocoder.fetchAdministrativeAddress(
    latitude: Double, longitude: Double, event: (address: String) -> Unit
) {

    val MAX_ADDRESSES_RESULT_NUMBER = 1
    val defaultAdministrativeArea = "x: $latitude, y: $longitude"

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // API 레벨 33 이상
        getFromLocation(
            latitude, longitude, MAX_ADDRESSES_RESULT_NUMBER
        ) { addresses: List<Address> ->
            if (addresses.isEmpty() || addresses[0].getAddressLine(0).isEmpty()) {
                event(defaultAdministrativeArea)
                return@getFromLocation
            }

            val address = addresses[0]
            val adminArea: String = address.adminArea ?: ""
            val subAdminArea: String = address.featureName ?: ""
            val administrativeArea = adminArea + subAdminArea

            if (administrativeArea != "") event(administrativeArea)
            else event(defaultAdministrativeArea)
        }
    } else { // API 레벨 33 미만
        val addresses: List<Address>? =
            getFromLocation(latitude, longitude, MAX_ADDRESSES_RESULT_NUMBER)

        if (addresses == null || addresses.isEmpty() || addresses[0].getAddressLine(0).isEmpty()) {
            event(defaultAdministrativeArea)
            return
        }

        val address = addresses[0]
        val adminArea: String = address.adminArea ?: ""
        val subAdminArea: String = address.subAdminArea ?: ""
        val administrativeArea = adminArea + subAdminArea

        if (administrativeArea != "") event(administrativeArea)
        else event(defaultAdministrativeArea)
    }
}

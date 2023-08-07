package com.teamtripdraw.android.support.framework.presentation.extensions

import android.location.Address
import android.location.Geocoder
import android.os.Build
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun Geocoder.fetchAddress(
    latitude: Double, longitude: Double, event: (address: String) -> Unit
) {
    val ADDRESSES_RESULT_MAX_NUMBER = 1
    val defaultAddress = "x: $latitude, y: $longitude"

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // API 레벨 33 이상
        getFromLocation(
            latitude, longitude, ADDRESSES_RESULT_MAX_NUMBER
        ) { addresses: List<Address> ->
            event(getAdministrativeAddress(addresses, defaultAddress))
        }
    } else { // API 레벨 33 미만
        val addresses: List<Address>? =
            getFromLocation(latitude, longitude, ADDRESSES_RESULT_MAX_NUMBER)
        event(getAdministrativeAddress(addresses, defaultAddress))
    }
}

private fun getAdministrativeAddress(addresses: List<Address>?, defaultAddress: String): String {
    if (addresses == null || addresses.isEmpty() || addresses[0].getAddressLine(0).isEmpty()) {
        return defaultAddress
    }

    val fullAddress = addresses[0].getAddressLine(0)
    val addressParts: MutableList<String> = fullAddress.split(" ").toMutableList()
    addressParts.removeFirst()
    addressParts.removeLast()
    val address = addressParts.joinToString(" ")

    return if (addressParts.isNotEmpty()) address
    else defaultAddress
}
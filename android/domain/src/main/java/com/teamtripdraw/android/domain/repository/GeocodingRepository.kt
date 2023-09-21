package com.teamtripdraw.android.domain.repository

interface GeocodingRepository {

    suspend fun getAddress(latitude: Double, longitude: Double): Result<String>
}

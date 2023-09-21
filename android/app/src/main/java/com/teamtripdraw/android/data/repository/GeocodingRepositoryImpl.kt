package com.teamtripdraw.android.data.repository

import com.teamtripdraw.android.data.dataSource.geocoding.GeocodingDataSource
import com.teamtripdraw.android.domain.repository.GeocodingRepository
import javax.inject.Inject

class GeocodingRepositoryImpl @Inject constructor(
    private val remoteGeocodingDataSource: GeocodingDataSource.Remote,
) : GeocodingRepository {

    override suspend fun getAddress(latitude: Double, longitude: Double): Result<String> {
        return remoteGeocodingDataSource.getReverseGeocoding(latitude, longitude).map {
            val result = java.lang.StringBuilder("")
            if (it.area1.isNotBlank()) result.append("${it.area1} ")
            if (it.area2.isNotBlank()) result.append("${it.area2} ")
            if (it.area3.isNotBlank()) result.append(it.area3)

            result.toString()
        }
    }
}

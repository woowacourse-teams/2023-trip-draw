package com.teamtripdraw.android.data.repository

import com.teamtripdraw.android.data.dataSource.geocoding.GeocodingDataSource
import com.teamtripdraw.android.domain.repository.GeocodingRepository
import javax.inject.Inject

class GeocodingRepositoryImpl @Inject constructor(
    private val remoteGeocodingDataSource: GeocodingDataSource.Remote,
) : GeocodingRepository {

    override suspend fun getAddress(latitude: Double, longitude: Double): Result<String> {
        return remoteGeocodingDataSource.getReverseGeocoding(latitude, longitude).map {
            "${it.area1} ${it.area2} ${it.area3}"
        }
    }
}

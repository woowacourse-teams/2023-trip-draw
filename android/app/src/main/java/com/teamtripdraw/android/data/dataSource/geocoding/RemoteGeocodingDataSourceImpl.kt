package com.teamtripdraw.android.data.dataSource.geocoding

import com.teamtripdraw.android.data.httpClient.service.NaverGeocodingService
import com.teamtripdraw.android.data.model.DataReverseGeocoding
import com.teamtripdraw.android.data.model.mapper.toData

class RemoteGeocodingDataSourceImpl(
    private val remoteGeocodingService: NaverGeocodingService,
) : GeocodingDataSource.Remote {

    override suspend fun getReverseGeocoding(
        latitude: Double,
        longitude: Double,
    ): Result<DataReverseGeocoding> {
        val coordsFormat = "%s,%s"
        return remoteGeocodingService.getReverseGeocoding(
            coords = coordsFormat.format(longitude, latitude),
        ).process { body, headers ->
            Result.success(body.toData())
        }
    }
}

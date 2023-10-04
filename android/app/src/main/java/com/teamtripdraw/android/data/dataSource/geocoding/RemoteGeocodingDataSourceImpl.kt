package com.teamtripdraw.android.data.dataSource.geocoding

import com.teamtripdraw.android.data.httpClient.service.NaverGeocodingService
import com.teamtripdraw.android.data.model.DataReverseGeocoding
import com.teamtripdraw.android.data.model.mapper.toData
import javax.inject.Inject

class RemoteGeocodingDataSourceImpl @Inject constructor(
    private val remoteGeocodingService: NaverGeocodingService,
) : GeocodingDataSource.Remote {

    override suspend fun getReverseGeocoding(
        latitude: Double,
        longitude: Double,
    ): Result<DataReverseGeocoding> {
        return remoteGeocodingService.getReverseGeocoding(
            coords = COORDS_FORMAT.format(longitude, latitude),
        ).process { body, headers ->
            Result.success(body.toData())
        }
    }

    companion object {
        private val COORDS_FORMAT = "%s,%s"
    }
}

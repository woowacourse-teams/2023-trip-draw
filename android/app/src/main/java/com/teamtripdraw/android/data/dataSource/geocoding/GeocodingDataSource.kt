package com.teamtripdraw.android.data.dataSource.geocoding

import com.teamtripdraw.android.data.model.DataReverseGeocoding

interface GeocodingDataSource {
    interface Remote {
        suspend fun getReverseGeocoding(
            latitude: Double,
            longitude: Double,
        ): Result<DataReverseGeocoding>
    }
}

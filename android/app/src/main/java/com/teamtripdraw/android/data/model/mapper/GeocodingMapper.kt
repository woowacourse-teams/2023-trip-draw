package com.teamtripdraw.android.data.model.mapper

import com.teamtripdraw.android.data.model.DataReverseGeocoding

fun com.teamtripdraw.android.data.httpClient.dto.response.GetReverseGeocodingResponse.toData(): DataReverseGeocoding {
    val region = this.results[0].region
    return DataReverseGeocoding(
        area1 = region.area1.name,
        area2 = region.area2.name,
        area3 = region.area3.name,
    )
}

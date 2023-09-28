package com.teamtripdraw.android.data.model.mapper

import com.teamtripdraw.android.data.httpClient.dto.response.GetReverseGeocodingResponse
import com.teamtripdraw.android.data.model.DataReverseGeocoding

fun GetReverseGeocodingResponse.toData(): DataReverseGeocoding {
    if (this.results.isEmpty()) return DataReverseGeocoding("", "", "")

    val region = this.results.first().region
    return DataReverseGeocoding(
        SiDo = region.area1.name,
        SiGunGu = region.area2.name,
        EupMyeonDong = region.area3.name,
    )
}

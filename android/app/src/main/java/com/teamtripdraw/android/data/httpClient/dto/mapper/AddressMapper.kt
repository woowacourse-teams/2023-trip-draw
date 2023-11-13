package com.teamtripdraw.android.data.httpClient.dto.mapper

import com.teamtripdraw.android.data.httpClient.dto.response.AllAddressResponse
import com.teamtripdraw.android.data.httpClient.dto.response.GetAllAddressesResponse
import com.teamtripdraw.android.data.model.DataAddress

fun GetAllAddressesResponse.toData() = this.areas.map { it.toData() }

fun AllAddressResponse.toData() =
    DataAddress(this.sido, this.sigungu, this.eupmyeondong)

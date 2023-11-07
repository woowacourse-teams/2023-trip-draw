package com.teamtripdraw.android.data.dataSource.address

import com.teamtripdraw.android.data.httpClient.dto.mapper.toData
import com.teamtripdraw.android.data.httpClient.service.GetAddressesService
import com.teamtripdraw.android.data.httpClient.service.GetAllAddressesService
import com.teamtripdraw.android.data.model.DataAddress
import javax.inject.Inject

class RemoteAddressDataSourceImpl @Inject constructor(
    private val getAddressesService: GetAddressesService,
    private val getAllAddressesService: GetAllAddressesService,
) : AddressDataSource.Remote {

    override suspend fun getAddresses(siDo: String, siGunGu: String): Result<List<String>> =
        getAddressesService.getAddresses(siDo, siGunGu)
            .process { body, headers -> Result.success(body.areas) }

    override suspend fun getAllAddresses(): Result<List<DataAddress>> =
        getAllAddressesService.getAddresses()
            .process { body, headers -> Result.success(body.toData()) }
}

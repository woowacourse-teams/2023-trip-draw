package com.teamtripdraw.android.data.dataSource.address

import com.teamtripdraw.android.data.httpClient.service.GetAddressesService
import javax.inject.Inject

class RemoteAddressDataSource @Inject constructor(
    private val getAddressesService: GetAddressesService,
) : AddressDataSource.Remote {

    override suspend fun getAddresses(siDo: String, siGunGu: String): Result<List<String>> =
        getAddressesService.getAddresses(siDo, siGunGu)
            .process { body, headers -> Result.success(body.areas) }
}

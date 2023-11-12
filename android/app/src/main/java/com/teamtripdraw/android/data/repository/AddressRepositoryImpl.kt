package com.teamtripdraw.android.data.repository

import com.teamtripdraw.android.data.dataSource.address.AddressDataSource
import com.teamtripdraw.android.domain.repository.AddressRepository
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(
    private val remoteAddressDataSource: AddressDataSource.Remote,
) : AddressRepository {

    override suspend fun getSiDos(): Result<List<String>> =
        remoteAddressDataSource.getAddresses("", "")

    override suspend fun getSiGunGus(siDo: String): Result<List<String>> =
        remoteAddressDataSource.getAddresses(siDo, "")

    override suspend fun getEupMyeonDongs(siDo: String, siGunGu: String): Result<List<String>> =
        remoteAddressDataSource.getAddresses(siDo, siGunGu)
}

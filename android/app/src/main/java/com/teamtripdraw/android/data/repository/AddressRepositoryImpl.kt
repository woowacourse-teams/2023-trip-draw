package com.teamtripdraw.android.data.repository

import com.teamtripdraw.android.data.dataSource.address.AddressDataSource
import com.teamtripdraw.android.data.localDatabase.mapper.toEntity
import com.teamtripdraw.android.data.model.DataAddress
import com.teamtripdraw.android.domain.repository.AddressRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(
    private val remoteAddressDataSource: AddressDataSource.Remote,
    private val localAddressDataSource: AddressDataSource.Local,
) : AddressRepository {

    override suspend fun getSiDos(): Result<List<String>> {
        if (localAddressDataSource.requiresUpdate()) {
            // 전부 받아오는 작업은 오래 걸리므로 새로운 코루틴을 사용합니다.
            CoroutineScope(Dispatchers.IO).launch { updateAddresses() }
            // 전부 받아오는 작업을 하는 동안 단일 조회를 사용해 비교적 빠르게 사용자에게 데이터를 제공합니다.
            return remoteAddressDataSource.getAddresses("", "")
        }
        return Result.success(localAddressDataSource.getSiDos())
    }

    override suspend fun getSiGunGus(siDo: String): Result<List<String>> {
        if (localAddressDataSource.requiresUpdate()) {
            // 시, 도에 대한 시군구를 요청하는 작업은 시, 도를 요구하는 작업입니다.
            // 아직 로컬에 주소 목록이 적용되지 않은 상황이라는 것은 업데이트 중이거나, 주소 목록 전체를 받아오는 작업에 문제가 생겼을 경우이므로 단일 조회로 요청합니다.
            return remoteAddressDataSource.getAddresses(siDo, "")
        }
        return Result.success(localAddressDataSource.getSiGunGus(siDo))
    }

    override suspend fun getEupMyeonDongs(siDo: String, siGunGu: String): Result<List<String>> {
        if (localAddressDataSource.requiresUpdate()) {
            return remoteAddressDataSource.getAddresses(siDo, siGunGu)
        }
        return Result.success(localAddressDataSource.getEupMyeonDongs(siDo, siGunGu))
    }

    private suspend fun updateAddresses(): Result<List<DataAddress>> {
        localAddressDataSource.deleteAll()
        val addressesResult = remoteAddressDataSource.getAllAddresses()
        addressesResult.onSuccess { addresses ->
            addresses.forEach { address ->
                localAddressDataSource.insert(address.toEntity())
            }
        }
        return addressesResult
    }
}

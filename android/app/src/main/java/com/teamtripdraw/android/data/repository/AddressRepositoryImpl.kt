package com.teamtripdraw.android.data.repository

import com.teamtripdraw.android.domain.repository.AddressRepository
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor() : AddressRepository {

    override suspend fun getAddresses(siDo: String, siGunGu: String): List<String> {
        return listOf("수달시", "희애시", "멧돼지시")
    }
}

package com.teamtripdraw.android.domain.repository

interface AddressRepository {

    suspend fun getAddresses(siDo: String, siGunGu: String): List<String>
}

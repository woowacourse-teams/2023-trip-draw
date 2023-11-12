package com.teamtripdraw.android.domain.repository

interface AddressRepository {

    suspend fun getSiDos(): Result<List<String>>

    suspend fun getSiGunGus(siDo: String): Result<List<String>>

    suspend fun getEupMyeonDongs(siDo: String, siGunGu: String): Result<List<String>>
}

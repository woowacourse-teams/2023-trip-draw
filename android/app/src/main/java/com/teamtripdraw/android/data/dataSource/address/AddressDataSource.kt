package com.teamtripdraw.android.data.dataSource.address

import com.teamtripdraw.android.data.localDatabase.entity.AddressEntity
import com.teamtripdraw.android.data.model.DataAddress
import java.time.LocalDateTime

interface AddressDataSource {
    interface Remote {
        suspend fun getAddresses(siDo: String, siGunGu: String): Result<List<String>>
        suspend fun getAllAddresses(): Result<List<DataAddress>>
    }

    interface Local {
        suspend fun getSiDos(): List<String>
        suspend fun getSiGunGus(siDo: String): List<String>
        suspend fun getEupMyeonDongs(siDo: String, siGunGus: String): List<String>
        suspend fun requiresUpdate(nowDateTime: LocalDateTime = LocalDateTime.now()): Boolean
        suspend fun insert(addressEntity: AddressEntity): Unit
        suspend fun insertAll(addressEntities: List<AddressEntity>): Unit
        suspend fun deleteAll(): Unit
    }
}

package com.teamtripdraw.android.data.dataSource.address

import com.teamtripdraw.android.data.localDatabase.dao.AddressDao
import com.teamtripdraw.android.data.localDatabase.entity.AddressEntity
import java.time.Duration
import java.time.LocalDateTime
import javax.inject.Inject

class LocalAddressDataSourceImpl @Inject constructor(
    private val addressDao: AddressDao,
) : AddressDataSource.Local {

    override suspend fun getSiDos(): List<String> {
        return addressDao.getSiDos().distinct()
    }

    override suspend fun getSiGunGus(siDo: String): List<String> {
        return addressDao.getSiGunGus(siDo).distinct()
    }

    override suspend fun getEupMyeonDongs(siDo: String, siGunGus: String): List<String> {
        return addressDao.getEupMyeonDongs(siDo, siGunGus).distinct()
    }

    override suspend fun requiresUpdate(nowDateTime: LocalDateTime): Boolean {
        val updatedDateTime: LocalDateTime = getUpdateDateTime() ?: return true
        val duration: Duration = Duration.between(updatedDateTime, nowDateTime)
        return REQUIRE_UPDATE_DURATION < duration.toDays()
    }

    override suspend fun insert(addressEntity: AddressEntity) {
        addressDao.insert(addressEntity)
    }

    override suspend fun insertAll(addressEntities: List<AddressEntity>) {
        addressDao.insertAll(addressEntities)
    }

    override suspend fun deleteAll() {
        addressDao.deleteAll()
    }

    private suspend fun getUpdateDateTime(): LocalDateTime? {
        if (addressDao.getCount() == 0) return null
        val formattedDateTime: String = addressDao.getOldestUpdateDate()
        return LocalDateTime.parse(formattedDateTime, AddressEntity.dateTimeFormatter)
    }

    companion object {
        private const val REQUIRE_UPDATE_DURATION = 30
    }
}

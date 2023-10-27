package com.teamtripdraw.android.data.localDatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.teamtripdraw.android.data.localDatabase.entity.AddressEntity

@Dao
interface AddressDao {

    @Query(
        """
        SELECT siDo 
        FROM $ADDRESS_TABLE_NAME 
        ORDER BY siDo ASC
        """,
    )
    suspend fun getSiDos(): List<String>

    @Query(
        """
        SELECT siGunGu 
        FROM $ADDRESS_TABLE_NAME 
        WHERE siDo = :siDo
        ORDER BY siGunGu ASC
        """,
    )
    suspend fun getSiGunGus(siDo: String): List<String>

    @Query(
        """
        SELECT eupMyeonDong 
        FROM $ADDRESS_TABLE_NAME 
        WHERE siDo = :siDo AND siGunGu = :siGunGu
        ORDER BY eupMyeonDong ASC
        """,
    )
    suspend fun getEupMyeonDongs(siDo: String, siGunGu: String): List<String>

    @Query("""SELECT MIN(formattedUpdateDateTime) FROM $ADDRESS_TABLE_NAME""")
    suspend fun getOldestUpdateDate(): String

    @Insert
    suspend fun insert(address: AddressEntity)

    @Insert
    suspend fun insertAll(addresses: List<AddressEntity>)

    @Query("DELETE FROM $ADDRESS_TABLE_NAME")
    suspend fun deleteAll()

    @Query("SELECT COUNT(id) FROM $ADDRESS_TABLE_NAME")
    suspend fun getCount(): Int

    companion object {
        const val ADDRESS_TABLE_NAME = "address"
    }
}

package com.teamtripdraw.android.data.localDatabase.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.teamtripdraw.android.data.localDatabase.dao.AddressDao.Companion.ADDRESS_TABLE_NAME
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity(tableName = ADDRESS_TABLE_NAME)
data class AddressEntity(
    val siDo: String,
    val siGunGu: String,
    val eupMyeonDong: String,
    val formattedUpdateDateTime: String = LocalDateTime.now().format(dateTimeFormatter),
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    companion object {
        val dateTimeFormatter: DateTimeFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    }
}

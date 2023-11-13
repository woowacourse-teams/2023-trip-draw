package com.teamtripdraw.android.data.localDatabase.mapper

import com.teamtripdraw.android.data.localDatabase.entity.AddressEntity
import com.teamtripdraw.android.data.model.DataAddress
import java.time.LocalDateTime

fun DataAddress.toEntity(updateDateTime: LocalDateTime = LocalDateTime.now()) =
    AddressEntity(
        siDo = this.siDo,
        siGunGu = this.siGunGu,
        eupMyeonDong = this.eupMyeonDong,
        formattedUpdateDateTime = updateDateTime.format(AddressEntity.dateTimeFormatter),
    )

package com.teamtripdraw.android.data.dataSource.address

import com.teamtripdraw.android.data.model.DataAddress

interface AddressDataSource {
    interface Remote {
        suspend fun getAddresses(siDo: String, siGunGu: String): Result<List<String>>
        suspend fun getAllAddresses(): Result<List<DataAddress>>
    }
}

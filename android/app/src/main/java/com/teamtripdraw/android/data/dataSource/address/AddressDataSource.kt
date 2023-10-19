package com.teamtripdraw.android.data.dataSource.address

interface AddressDataSource {
    interface Remote {
        suspend fun getAddresses(siDo: String, siGunGu: String): Result<List<String>>
    }
}

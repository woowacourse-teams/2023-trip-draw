package com.teamtripdraw.android.data.dataSource.unsubscribe

interface UnsubscribeDataSource {
    interface Local
    interface Remote {
        suspend fun unsubscribe(): Result<Unit>
    }
}

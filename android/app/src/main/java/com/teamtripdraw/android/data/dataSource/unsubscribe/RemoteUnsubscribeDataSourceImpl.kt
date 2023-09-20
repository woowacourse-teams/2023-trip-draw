package com.teamtripdraw.android.data.dataSource.unsubscribe

import com.teamtripdraw.android.data.httpClient.service.UnsubscribeService
import javax.inject.Inject

class RemoteUnsubscribeDataSourceImpl @Inject constructor(
    private val unsubscribeService: UnsubscribeService,
) : UnsubscribeDataSource.Remote {
    override suspend fun unsubscribe(): Result<Unit> =
        unsubscribeService.unsubscribe().process { _, _ -> Result.success(Unit) }
}

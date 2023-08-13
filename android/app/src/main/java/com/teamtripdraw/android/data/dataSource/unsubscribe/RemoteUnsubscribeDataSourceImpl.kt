package com.teamtripdraw.android.data.dataSource.unsubscribe

import com.teamtripdraw.android.data.httpClient.service.UnsubscribeService

class RemoteUnsubscribeDataSourceImpl(
    private val unsubscribeService: UnsubscribeService,
) : UnsubscribeDataSource.Remote {
    override suspend fun unsubscribe(accessToken: String): Result<Unit> =
        unsubscribeService.unsubscribe(accessToken).process { _, _ -> Result.success(Unit) }
}

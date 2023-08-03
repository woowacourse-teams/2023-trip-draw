package com.teamtripdraw.android.data.dataSource.point

import com.teamtripdraw.android.data.httpClient.dto.mapper.toData
import com.teamtripdraw.android.data.httpClient.dto.mapper.toHttpRequest
import com.teamtripdraw.android.data.httpClient.service.CreateRecordingPointService
import com.teamtripdraw.android.data.httpClient.service.GetPointService
import com.teamtripdraw.android.data.model.DataPoint
import com.teamtripdraw.android.data.model.DataPrePoint

class RemotePointDataSourceImpl(
    private val createRecordingPointService: CreateRecordingPointService,
    private val getPointService: GetPointService
) : PointDataSource.Remote {

    override suspend fun createRecordingPoint(
        dataPrePoint: DataPrePoint,
        tripId: Long
    ): Result<Long> =
        createRecordingPointService.createRecordingPoint(dataPrePoint.toHttpRequest(tripId))
            .process { body, headers -> Result.success(body.pointId) }

    override suspend fun getPint(tripId: Long, pointId: Long): Result<DataPoint> =
        getPointService.getPoint(tripId = tripId, pointId = pointId)
            .process { body, headers -> Result.success(body.toData()) }

}

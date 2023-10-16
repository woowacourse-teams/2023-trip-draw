package com.teamtripdraw.android.data.dataSource.point

import com.teamtripdraw.android.data.httpClient.dto.mapper.toData
import com.teamtripdraw.android.data.httpClient.dto.mapper.toHttpRequest
import com.teamtripdraw.android.data.httpClient.service.CreateRecordingPointService
import com.teamtripdraw.android.data.httpClient.service.DeletePointService
import com.teamtripdraw.android.data.httpClient.service.GetPointPostService
import com.teamtripdraw.android.data.httpClient.service.GetPointService
import com.teamtripdraw.android.data.model.DataPoint
import com.teamtripdraw.android.data.model.DataPost
import com.teamtripdraw.android.data.model.DataPrePoint
import javax.inject.Inject

class RemotePointDataSourceImpl @Inject constructor(
    private val createRecordingPointService: CreateRecordingPointService,
    private val getPointService: GetPointService,
    private val getPointPostService: GetPointPostService,
    private val deletePointService: DeletePointService,
) : PointDataSource.Remote {

    override suspend fun createRecordingPoint(
        dataPrePoint: DataPrePoint,
        tripId: Long,
    ): Result<Long> =
        createRecordingPointService.createRecordingPoint(dataPrePoint.toHttpRequest(tripId))
            .process { body, headers -> Result.success(body.pointId) }

    override suspend fun getPoint(tripId: Long, pointId: Long): Result<DataPoint> =
        getPointService.getPoint(tripId = tripId, pointId = pointId)
            .process { body, headers -> Result.success(body.toData()) }

    override suspend fun getPost(pointId: Long): Result<DataPost> =
        getPointPostService.getPost(pointId)
            .process { body, headers -> Result.success(body.toData()) }

    override suspend fun deletePoint(tripId: Long, pointId: Long): Result<Unit> =
        deletePointService.deletePoint(pointId, tripId)
            .process { _, _ -> Result.success(Unit) }
}

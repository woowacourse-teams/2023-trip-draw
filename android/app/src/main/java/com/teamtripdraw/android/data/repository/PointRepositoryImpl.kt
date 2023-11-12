package com.teamtripdraw.android.data.repository

import com.teamtripdraw.android.data.dataSource.point.PointDataSource
import com.teamtripdraw.android.data.dataSource.trip.TripDataSource
import com.teamtripdraw.android.data.model.mapper.toData
import com.teamtripdraw.android.data.model.mapper.toDomain
import com.teamtripdraw.android.domain.model.point.Point
import com.teamtripdraw.android.domain.model.point.PrePoint
import com.teamtripdraw.android.domain.model.post.Post
import com.teamtripdraw.android.domain.repository.PointRepository
import javax.inject.Inject

class PointRepositoryImpl @Inject constructor(
    private val pointDataSource: PointDataSource.Remote,
    private val tripDataSource: TripDataSource.Local,
) : PointRepository {
    override suspend fun createRecordingPoint(prePoint: PrePoint, tripId: Long?): Result<Long> =
        pointDataSource.createRecordingPoint(
            prePoint.toData(),
            tripId ?: tripDataSource.getCurrentTripId(),
        )

    override suspend fun getPoint(pointId: Long, tripId: Long): Result<Point> =
        pointDataSource.getPoint(tripId = tripId, pointId = pointId).map { it.toDomain() }

    override suspend fun deletePoint(tripId: Long, pointId: Long): Result<Unit> =
        pointDataSource.deletePoint(tripId = tripId, pointId = pointId)
}

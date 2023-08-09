package com.teamtripdraw.android.domain.repository

import com.teamtripdraw.android.domain.model.point.Point
import com.teamtripdraw.android.domain.model.point.PrePoint

interface PointRepository {
    suspend fun createRecordingPoint(prePoint: PrePoint, tripId: Long? = null): Result<Long>
    suspend fun getPoint(pointId: Long, tripId: Long): Result<Point>
    suspend fun deletePoint(tripId: Long, pointId: Long): Result<Unit>
}

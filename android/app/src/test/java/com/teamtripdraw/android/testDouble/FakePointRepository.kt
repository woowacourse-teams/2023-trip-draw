package com.teamtripdraw.android.testDouble

import com.teamtripdraw.android.domain.model.point.Point
import com.teamtripdraw.android.domain.model.point.PrePoint
import com.teamtripdraw.android.domain.repository.PointRepository
import java.time.LocalDateTime

internal class FakePointRepository : PointRepository {
    val points: MutableList<Point> = mutableListOf()

    override suspend fun createRecordingPoint(prePoint: PrePoint, tripId: Long?): Result<Long> {
        val point = Point(
            pointId = points.size.toLong(),
            latitude = prePoint.latitude,
            longitude = prePoint.longitude,
            hasPost = false,
            recordedAt = LocalDateTime.of(2023, 2, 7, 10, 0),
        )
        points.add(point)
        return Result.success(point.pointId)
    }

    override suspend fun getPoint(pointId: Long, tripId: Long): Result<Point> {
        val point: Point? = points.find { it.pointId == pointId }
        return if (point != null) {
            Result.success(value = point)
        } else {
            Result.failure(NullPointerException())
        }
    }

    override suspend fun deletePoint(tripId: Long, pointId: Long): Result<Unit> {
        points.removeIf { it.pointId == pointId }
        return Result.success(Unit) // todo failure
    }
}

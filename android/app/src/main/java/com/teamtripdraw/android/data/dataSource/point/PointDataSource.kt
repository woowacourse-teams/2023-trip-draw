package com.teamtripdraw.android.data.dataSource.point

import com.teamtripdraw.android.data.model.DataPoint
import com.teamtripdraw.android.data.model.DataPrePoint

interface PointDataSource {
    interface Local
    interface Remote {
        suspend fun createRecordingPoint(dataPrePoint: DataPrePoint, tripId: Long): Result<Long>
        suspend fun getPoint(tripId: Long, pointId: Long): Result<DataPoint>
        suspend fun deletePoint(tripId: Long, pointId: Long): Result<Unit>
    }
}

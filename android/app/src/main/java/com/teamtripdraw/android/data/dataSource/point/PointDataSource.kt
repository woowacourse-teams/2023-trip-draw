package com.teamtripdraw.android.data.dataSource.point

import com.teamtripdraw.android.data.model.DataPrePoint

interface PointDataSource {
    interface Local
    interface Remote {
        suspend fun createRecordingPoint(dataPrePoint: DataPrePoint, tripId: Long): Result<Long>
    }
}

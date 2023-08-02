package com.teamtripdraw.android.domain.repository

import com.teamtripdraw.android.domain.model.point.PrePoint

interface PointRepository {
    suspend fun createRecordingPoint(prePoint: PrePoint, tripId: Long? = null): Result<Long>
}

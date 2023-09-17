package com.teamtripdraw.android.data.dataSource.trip

import com.teamtripdraw.android.data.model.DataPreSetTripTitle
import com.teamtripdraw.android.data.model.DataPreviewTrip
import com.teamtripdraw.android.data.model.DataTrip

interface TripDataSource {
    interface Local {
        fun setCurrentTripId(tripId: Long)

        fun getCurrentTripId(): Long

        fun deleteCurrentTripId()
    }

    interface Remote {
        suspend fun startTrip(): Result<Long>
        suspend fun getTripInfo(tripId: Long): Result<DataTrip>
        suspend fun setTripTitle(
            tripId: Long,
            dataPreSetTripTitle: DataPreSetTripTitle,
        ): Result<Unit>

        suspend fun getAllTrips(): Result<List<DataPreviewTrip>>
        suspend fun deleteTrip(tripId: Long): Result<Unit>
    }
}

package com.teamtripdraw.android.data.dataSource.trip

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
    }
}

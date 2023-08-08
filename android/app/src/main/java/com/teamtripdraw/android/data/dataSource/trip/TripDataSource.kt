package com.teamtripdraw.android.data.dataSource.trip

import com.teamtripdraw.android.data.model.DataTrip
import com.teamtripdraw.android.domain.model.trip.TripStatus

interface TripDataSource {
    interface Local {
        fun setCurrentTripId(tripId: Long)

        fun getCurrentTripId(): Long

        fun deleteCurrentTripId()
    }

    interface Remote {
        suspend fun startTrip(): Result<Long>
        suspend fun getTripInfo(tripId: Long): Result<DataTrip>
        suspend fun setTripTitle(tripId: Long, name: String, status: TripStatus): Result<Unit>
    }
}

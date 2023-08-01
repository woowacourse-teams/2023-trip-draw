package com.teamtripdraw.android.data.dataSource.trip

interface TripDataSource {
    interface Local {
        fun setCurrentTripId(tripId: Long)

        fun getCurrentTripId(): Long

        fun deleteCurrentTripId()
    }

    interface Remote {
        suspend fun startTrip(): Result<Long>
    }
}

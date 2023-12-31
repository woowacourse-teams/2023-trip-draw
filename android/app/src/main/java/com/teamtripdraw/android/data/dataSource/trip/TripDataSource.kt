package com.teamtripdraw.android.data.dataSource.trip

import com.teamtripdraw.android.data.model.DataPreSetTripTitle
import com.teamtripdraw.android.data.model.DataPreviewTrip
import com.teamtripdraw.android.data.model.DataTrip
import com.teamtripdraw.android.data.model.DataTripOfAll

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

        suspend fun getMyTrips(): Result<List<DataPreviewTrip>>
        suspend fun getAllTrips(
            address: String,
            ageRanges: List<Int>,
            daysOfWeek: List<Int>,
            genders: List<Int>,
            months: List<Int>,
            years: List<Int>,
            lastViewedId: Long?,
            limit: Int,
        ): Result<List<DataTripOfAll>>

        suspend fun deleteTrip(tripId: Long): Result<Unit>
    }
}

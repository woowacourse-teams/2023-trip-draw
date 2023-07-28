package com.teamtripdraw.android.data.dataSource.createTrip

import android.content.SharedPreferences
import androidx.core.content.edit
import com.teamtripdraw.android.domain.constants.NULL_SUBSTITUTE_TRIP_ID

class LocalTripDataSourceImpl(private val tripPreference: SharedPreferences) :
    TripDataSource.Local {
    override fun setTripId(tripId: Long) {
        tripPreference.edit { putLong(TRIP_ID, tripId) }
    }

    override fun getTripId(): Long =
        tripPreference.getLong(TRIP_ID, NULL_SUBSTITUTE_TRIP_ID)

    override fun deleteTripId() {
        tripPreference.edit { remove(TRIP_ID) }
    }

    companion object TripKey {
        private const val TRIP_ID = "TRIP_ID"
    }
}

package com.teamtripdraw.android.data.dataSource.trip

import android.content.SharedPreferences
import androidx.core.content.edit
import com.teamtripdraw.android.di.qualifier.TripPreference
import com.teamtripdraw.android.domain.model.trip.Trip
import javax.inject.Inject

class LocalTripDataSourceImpl @Inject constructor(@TripPreference private val tripPreference: SharedPreferences) :
    TripDataSource.Local {
    override fun setCurrentTripId(tripId: Long) {
        tripPreference.edit { putLong(CURRENT_TRIP_ID, tripId) }
    }

    override fun getCurrentTripId(): Long =
        tripPreference.getLong(CURRENT_TRIP_ID, Trip.NULL_SUBSTITUTE_ID)

    override fun deleteCurrentTripId() {
        tripPreference.edit { remove(CURRENT_TRIP_ID) }
    }

    companion object TripKey {
        private const val CURRENT_TRIP_ID = "CURRENT_TRIP_ID"
    }
}

package com.teamtripdraw.android.ui.home.recordingPoint

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.teamtripdraw.android.domain.constants.NULL_SUBSTITUTE_TRIP_ID

class RecordingPointService : Service() {
    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val tripId = intent.getLongExtra(TRIP_ID, NULL_SUBSTITUTE_TRIP_ID)
        setRecordingPoint(tripId)
        return START_REDELIVER_INTENT
    }

    private fun setRecordingPoint(tripId: Long) {
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        private const val TRIP_ID = "TRIP_ID"

        fun getInfoPackedIntent(intent: Intent, tripId: Long): Intent =
            intent.apply { putExtra(TRIP_ID, tripId) }
    }
}

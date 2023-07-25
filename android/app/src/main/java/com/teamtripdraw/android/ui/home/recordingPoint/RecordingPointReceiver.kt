package com.teamtripdraw.android.ui.home.recordingPoint

import android.content.Intent

class RecordingPointReceiver {

    companion object {
        private const val TRIP_ID = "TRIP_ID"

        fun getInfoPackedIntent(intent: Intent, tripId: Long): Intent =
            intent.apply { putExtra(TRIP_ID, tripId) }
    }
}

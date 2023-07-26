package com.teamtripdraw.android.ui.home.recordingPoint

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import java.util.concurrent.TimeUnit

class RecordingPointAlarmManager(private val context: Context) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val intent = Intent(context, RecordingPointService::class.java)

    fun startRecord(tripId: Long) {
        val pendingIntent = PendingIntent.getService(
            context,
            RECORD_REQUEST_CODE,
            RecordingPointService.getInfoPackedIntent(intent, tripId),
            PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime(),
            RECORD_INTERVAL_TIME,
            pendingIntent
        )
    }

    fun cancelRecord() {
        val pendingIntent = PendingIntent.getService(
            context,
            RECORD_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_NO_CREATE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
        }
    }

    companion object {
        private const val RECORD_REQUEST_CODE = 1000
        private val RECORD_INTERVAL_TIME = TimeUnit.MINUTES.toMillis(10)
    }
}

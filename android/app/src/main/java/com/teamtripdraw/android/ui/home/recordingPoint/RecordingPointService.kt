package com.teamtripdraw.android.ui.home.recordingPoint

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.teamtripdraw.android.R
import com.teamtripdraw.android.domain.constants.NULL_SUBSTITUTE_TRIP_ID

class RecordingPointService : Service() {
    override fun onCreate() {
        super.onCreate()
        initNotificationChannelToNotificationManager()
        startForeground(NOTIFICATION_ID, initNotification())
    }

    private fun initNotification(): Notification =
        NotificationCompat.Builder(
            this,
            this.getString(R.string.recording_point_service_alarm_channel_id)
        )
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_home) // todo 팀아이콘으로 변경 해야 한다.
            .setShowWhen(false)
            .setContentTitle("여행중입니다.") // todo 문구 기획측으로 부터 받아서 변경 해야 한다.
            .build()

    private fun initNotificationChannelToNotificationManager() {
        val notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(setNotificationChannel())
    }

    private fun setNotificationChannel(): NotificationChannel {
        val channelID = this.getString(R.string.recording_point_service_alarm_channel_id)
        val channelName = this.getString(R.string.recording_point_service_alarm_channel_name)
        val channelDescription =
            this.getString(R.string.recording_point_service_alarm_channel_description)
        val importance = NotificationManager.IMPORTANCE_HIGH
        return NotificationChannel(channelID, channelName, importance).apply {
            description = channelDescription
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val tripId = intent.getLongExtra(TRIP_ID, NULL_SUBSTITUTE_TRIP_ID)
        setRecordingPoint(tripId)
        return START_REDELIVER_INTENT
    }

    private fun setRecordingPoint(tripId: Long) {
        Log.d("멧돼지", "서비스 실행되고있음")
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        private const val TRIP_ID = "TRIP_ID"
        private const val NOTIFICATION_ID = 1001

        fun getInfoPackedIntent(intent: Intent, tripId: Long): Intent =
            intent.apply { putExtra(TRIP_ID, tripId) }
    }
}

package com.teamtripdraw.android.ui.home.recordingPoint

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.teamtripdraw.android.R
import com.teamtripdraw.android.TripDrawApplication
import com.teamtripdraw.android.domain.constants.NULL_SUBSTITUTE_TRIP_ID
import com.teamtripdraw.android.domain.model.point.PrePoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import kotlin.properties.Delegates

class RecordingPointService : Service() {

    private var currentTripId by Delegates.notNull<Long>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            recordPoint(locationResult)
            finishUpdateLocation()
        }
    }

    private fun recordPoint(locationResult: LocationResult) {
        CoroutineScope(Dispatchers.IO).launch {
            TripDrawApplication.repositoryContainer.pointRepository.createRecordingPoint(
                getPrePoint(locationResult),
                currentTripId
            ).onSuccess {
                // todo 액티비티 위치 찍어주기
            }.onFailure {
                // todo log전략 수립후 서버로 전송되는 로그 찍기
            }
        }
    }

    private fun getPrePoint(locationResult: LocationResult): PrePoint {
        val prePoint: PrePoint = PrePoint(
            locationResult.locations.first().latitude,
            locationResult.locations.first().longitude,
            LocalDateTime.now()
        )
        return prePoint
    }

    private fun finishUpdateLocation() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onCreate() {
        super.onCreate()
        initNotificationChannelToNotificationManager()
        startForeground(NOTIFICATION_ID, initNotification())
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
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
        currentTripId = intent.getLongExtra(TRIP_ID, NULL_SUBSTITUTE_TRIP_ID)
        if (currentTripId != NULL_SUBSTITUTE_TRIP_ID) {
            startUpdateLocation()
        } else {
            // todo 로그전략 수립후 잘못된 tripID임을 명시하는 로그 작성 #134
        }
        return START_REDELIVER_INTENT
    }

    private fun startUpdateLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(
            getLocationRequest(),
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun getLocationRequest() =
        LocationRequest.create().apply {
            interval = LOCATION_REQUEST_INTERVAL
            fastestInterval = LOCATION_REQUEST_FASTEST_INTERVAL
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        private const val TRIP_ID = "TRIP_ID"
        private const val NOTIFICATION_ID = 1001
        private const val LOCATION_REQUEST_INTERVAL = 100L
        private const val LOCATION_REQUEST_FASTEST_INTERVAL = 10L

        fun getTripIdIntent(intent: Intent, tripId: Long): Intent =
            intent.apply { putExtra(TRIP_ID, tripId) }
    }
}

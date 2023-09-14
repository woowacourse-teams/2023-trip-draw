package com.teamtripdraw.android.ui.home.recordingPoint

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.teamtripdraw.android.R
import com.teamtripdraw.android.TripDrawApplication
import com.teamtripdraw.android.TripDrawApplication.DependencyContainer.logUtil
import com.teamtripdraw.android.domain.constants.NULL_SUBSTITUTE_POINT_ID
import com.teamtripdraw.android.domain.model.point.PrePoint
import com.teamtripdraw.android.domain.model.trip.Trip
import com.teamtripdraw.android.support.framework.presentation.Locations.getUpdateLocation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import kotlin.properties.Delegates

class RecordingPointService : Service() {

    private var currentTripId by Delegates.notNull<Long>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val _updatedTripId = MutableLiveData<Long>(NULL_SUBSTITUTE_POINT_ID)
    val updatedTripId: LiveData<Long> = _updatedTripId

    private fun recordPoint(locationResult: LocationResult) {
        CoroutineScope(Dispatchers.IO).launch {
            TripDrawApplication.repositoryContainer.pointRepository.createRecordingPoint(
                getPrePoint(locationResult),
                currentTripId,
            ).onSuccess {
                _updatedTripId.postValue(it)
            }.onFailure {
                logUtil.general.log(it)
            }
        }
    }

    private fun getPrePoint(locationResult: LocationResult): PrePoint {
        val prePoint: PrePoint = PrePoint(
            locationResult.locations.first().latitude,
            locationResult.locations.first().longitude,
            LocalDateTime.now(),
        )
        return prePoint
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
            this.getString(R.string.recording_point_service_alarm_channel_id),
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
        currentTripId = intent.getLongExtra(TRIP_ID, Trip.NULL_SUBSTITUTE_ID)
        if (currentTripId != Trip.NULL_SUBSTITUTE_ID) {
            getUpdateLocation(fusedLocationClient, this, this::recordPoint)
        } else {
            logUtil.general.log(message = WRONG_TRIP_ID_ERROR)
        }
        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent?): IBinder? = RecordingPointBinder()

    inner class RecordingPointBinder : Binder() {
        fun getUpdatedTripIdHolder(): LiveData<Long> = updatedTripId
        fun updatedTripIdHolderInitializeState() {
            _updatedTripId.value = NULL_SUBSTITUTE_POINT_ID
        }
    }

    companion object {
        private const val TRIP_ID = "TRIP_ID"
        private const val NOTIFICATION_ID = 1001
        private const val WRONG_TRIP_ID_ERROR = "RecrodingPointService로 Null 대응 tripId가 전달되었습니다."

        fun getTripIdIntent(intent: Intent, tripId: Long): Intent =
            intent.apply { putExtra(TRIP_ID, tripId) }
    }
}

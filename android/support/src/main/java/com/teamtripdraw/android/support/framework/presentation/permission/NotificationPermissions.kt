package com.teamtripdraw.android.support.framework.presentation.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager.*
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private const val POST_NOTIFICATION = Manifest.permission.POST_NOTIFICATIONS

fun checkNotificationPermission(
    context: Context,
    activityResultLauncher: ActivityResultLauncher<String>
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val notificationState =
            ContextCompat.checkSelfPermission(context, POST_NOTIFICATION) == PERMISSION_GRANTED
        processNotificationPermission(notificationState, activityResultLauncher)
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun processNotificationPermission(
    notificationState: Boolean,
    activityResultLauncher: ActivityResultLauncher<String>
) {
    if (!notificationState) {
        activityResultLauncher.launch(POST_NOTIFICATION)
    }
}

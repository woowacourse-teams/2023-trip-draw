package com.teamtripdraw.android.support.framework.presentation.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat

private const val CAMERA = Manifest.permission.CAMERA
private const val WRITE_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE

fun checkCameraPermission(context: Context): Boolean {
    val hasCameraPermission: Boolean = hasCameraPermission(context)

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        hasCameraPermission
    } else {
        val hasWriteStoragePermission = hasWriteStoragePermission(context)
        hasCameraPermission && hasWriteStoragePermission
    }
}

fun requestCameraPermission(
    context: Context,
    activityResultLauncher: ActivityResultLauncher<Array<String>>,
) {
    val hasCameraPermission = hasCameraPermission(context)
    val hasWriteStoragePermission = hasWriteStoragePermission(context)

    processCameraPermission(
        hasWriteStoragePermission,
        hasCameraPermission,
        activityResultLauncher,
    )
}

private fun processCameraPermission(
    hasWriteStoragePermission: Boolean,
    hasCameraPermission: Boolean,
    activityResultLauncher: ActivityResultLauncher<Array<String>>,
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && hasCameraPermission.not()) {
        activityResultLauncher.launch(arrayOf(CAMERA))
        return
    }
    if (hasWriteStoragePermission.not() || hasCameraPermission.not()) {
        activityResultLauncher.launch(arrayOf(CAMERA, WRITE_STORAGE))
    }
}

private fun hasCameraPermission(context: Context) =
    ContextCompat.checkSelfPermission(context, CAMERA) == PackageManager.PERMISSION_GRANTED

private fun hasWriteStoragePermission(context: Context) =
    ContextCompat.checkSelfPermission(context, WRITE_STORAGE) == PackageManager.PERMISSION_GRANTED

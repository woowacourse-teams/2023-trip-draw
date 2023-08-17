package com.teamtripdraw.android.support.framework.presentation.images

import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import java.text.SimpleDateFormat
import java.util.*

private const val SIMPLE_DATE_FORMAT = "yyMMdd_HHmmss"

fun createImageUri(contentResolver: ContentResolver): Uri? {
    val now = SimpleDateFormat(SIMPLE_DATE_FORMAT).format(Date())
    val content = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "img_$now.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
    }
    return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, content)
}

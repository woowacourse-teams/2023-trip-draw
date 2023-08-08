package com.teamtripdraw.android.support.framework.presentation.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

fun Uri.toResizedImageFile(context: Context): File? {
    val bitmap = toBitmap(context) ?: return null
    val file = getResizedImageFile(bitmap, context.cacheDir)
    val orientation = context.contentResolver
        .openInputStream(this)?.use {
            ExifInterface(it)
        }?.getAttribute(ExifInterface.TAG_ORIENTATION)

    orientation?.let { file.setOrientation(it) }

    return file
}

private fun Uri.toBitmap(context: Context): Bitmap? {
    return context.contentResolver
        .openInputStream(this)?.use {
            BitmapFactory.decodeStream(it)
        }
}

private fun getResizedImageFile(bitmap: Bitmap, directory: File): File {
    val byteArrayOutputStream = ByteArrayOutputStream()
    val resizedBitmap: Bitmap = getResizedBitmap(bitmap)
    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)

    val tempFile = File.createTempFile("image", ".jpg", directory)
    FileOutputStream(tempFile).use {
        it.write(byteArrayOutputStream.toByteArray())
    }
    return tempFile
}

private fun getResizedBitmap(bitmap: Bitmap): Bitmap {
    val RESIZE_SIZE = 720f
    var resizedWidth: Float = RESIZE_SIZE
    var resizedHeight: Float = RESIZE_SIZE

    if (bitmap.width >= RESIZE_SIZE && bitmap.height >= RESIZE_SIZE) {
        if (bitmap.width >= bitmap.height) {
            resizedWidth = RESIZE_SIZE * (bitmap.width.toFloat() / bitmap.height.toFloat())
        } else {
            resizedHeight = RESIZE_SIZE * (bitmap.height.toFloat() / bitmap.width.toFloat())
        }
    } else {
        resizedWidth = bitmap.width.toFloat()
        resizedHeight = bitmap.height.toFloat()
    }

    return Bitmap.createScaledBitmap(
        bitmap, resizedWidth.toInt(), resizedHeight.toInt(), true
    )
}

private fun File.setOrientation(orientation: String) {
    ExifInterface(this.path).apply {
        setAttribute(ExifInterface.TAG_ORIENTATION, orientation)
        saveAttributes()
    }
}
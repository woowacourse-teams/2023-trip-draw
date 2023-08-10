package com.teamtripdraw.android.support.framework.presentation.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

val DEFAULT_IMAGE_QUALITY = 90
val DEFAULT_RESIZE_SIZE = 720f

fun Uri.toResizedImageFile(context: Context): File? {
    val bitmap = toBitmap(context) ?: return null
    val file = getResizedImageFile(bitmap = bitmap, directory = context.cacheDir)
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

private fun getResizedImageFile(
    bitmap: Bitmap,
    directory: File,
    quality: Int = DEFAULT_IMAGE_QUALITY,
): File {
    val byteArrayOutputStream = ByteArrayOutputStream()
    val resizedBitmap: Bitmap = getResizedBitmap(bitmap = bitmap)
    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)

    val file = File.createTempFile("image", ".jpg", directory)
    FileOutputStream(file).use {
        it.write(byteArrayOutputStream.toByteArray())
    }
    return file
}

private fun getResizedBitmap(
    bitmap: Bitmap,
    resizeSize: Float = DEFAULT_RESIZE_SIZE,
): Bitmap {
    var resizedWidth: Float = resizeSize
    var resizedHeight: Float = resizeSize

    if (bitmap.width >= resizeSize && bitmap.height >= resizeSize) {
        if (bitmap.width >= bitmap.height) {
            resizedWidth = resizeSize * (bitmap.width.toFloat() / bitmap.height.toFloat())
        } else {
            resizedHeight = resizeSize * (bitmap.height.toFloat() / bitmap.width.toFloat())
        }
    } else {
        resizedWidth = bitmap.width.toFloat()
        resizedHeight = bitmap.height.toFloat()
    }

    return Bitmap.createScaledBitmap(
        bitmap,
        resizedWidth.toInt(),
        resizedHeight.toInt(),
        true,
    )
}

private fun File.setOrientation(orientation: String) {
    ExifInterface(this.path).apply {
        setAttribute(ExifInterface.TAG_ORIENTATION, orientation)
        saveAttributes()
    }
}

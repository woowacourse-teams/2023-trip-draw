package com.teamtripdraw.android.support.framework.presentation.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

private const val DEFAULT_IMAGE_QUALITY = 90
private const val DEFAULT_RESIZE_SIZE = 720f
private val DEFAULT_IMAGE_FORMAT = Bitmap.CompressFormat.JPEG

private const val WRONG_FILE_EXTENSION_MESSAGE = "등록되지 않은 파일 형식이으로 이미지 파일로 변환할 수 없습니다."

fun Uri.toResizedImageFile(
    context: Context,
    resizeSize: Float = DEFAULT_RESIZE_SIZE,
    quality: Int = DEFAULT_IMAGE_QUALITY,
    imageFormat: Bitmap.CompressFormat = DEFAULT_IMAGE_FORMAT,
): File? {
    val bitmap = toBitmap(context) ?: return null
    val file =
        bitmap.getResizedImageFile(
            quality = quality,
            directory = context.cacheDir,
            resizeSize = resizeSize,
            imageFormat = imageFormat,
        )
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

private fun Bitmap.getResizedImageFile(
    directory: File,
    quality: Int,
    resizeSize: Float,
    imageFormat: Bitmap.CompressFormat,
): File {
    val byteArrayOutputStream = ByteArrayOutputStream()
    val resizedBitmap: Bitmap = getResizedBitmap(resizeSize = resizeSize)
    resizedBitmap.compress(imageFormat, quality, byteArrayOutputStream)

    val file = File.createTempFile("image", getImageFileExtension(imageFormat), directory)
    FileOutputStream(file).use {
        it.write(byteArrayOutputStream.toByteArray())
    }
    return file
}

private fun getImageFileExtension(imageFormat: Bitmap.CompressFormat): String {
    return when (imageFormat) {
        Bitmap.CompressFormat.JPEG -> ".jpg"
        Bitmap.CompressFormat.PNG -> ".png"
        else -> throw IllegalArgumentException(WRONG_FILE_EXTENSION_MESSAGE)
    }
}

private fun Bitmap.getResizedBitmap(
    resizeSize: Float,
): Bitmap {
    var resizedWidth: Float = resizeSize
    var resizedHeight: Float = resizeSize

    if (width >= resizeSize && height >= resizeSize) {
        if (width >= height) {
            resizedWidth = resizeSize * (width.toFloat() / height.toFloat())
        } else {
            resizedHeight = resizeSize * (height.toFloat() / width.toFloat())
        }
    } else {
        resizedWidth = width.toFloat()
        resizedHeight = height.toFloat()
    }

    return Bitmap.createScaledBitmap(this, resizedWidth.toInt(), resizedHeight.toInt(), true)
}

private fun File.setOrientation(orientation: String) {
    ExifInterface(this.path).apply {
        setAttribute(ExifInterface.TAG_ORIENTATION, orientation)
        saveAttributes()
    }
}

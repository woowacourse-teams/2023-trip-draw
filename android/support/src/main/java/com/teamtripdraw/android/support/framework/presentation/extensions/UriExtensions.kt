package com.teamtripdraw.android.support.framework.presentation.extensions

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import java.io.File

fun Uri.toFile(context: Context): File? {
    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
    val contentResolver = context.contentResolver
    val cursor = contentResolver.query(this, filePathColumn, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val columnIndex = it.getColumnIndex(filePathColumn[0])
            val filePath = it.getString(columnIndex)
            return File(filePath)
        }
    }
    return null
}

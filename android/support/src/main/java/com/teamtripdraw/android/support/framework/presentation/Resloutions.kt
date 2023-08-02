package com.teamtripdraw.android.support.framework.presentation.resolution

import android.content.Context
import android.util.DisplayMetrics
import androidx.annotation.Px
import kotlin.math.roundToInt

@Px
fun toPixel(context: Context, dp: Int): Int =
    (dp * getDisplayMetrics(context).density).roundToInt()

fun toDP(context: Context, @Px pixel: Int): Int =
    (pixel / getDisplayMetrics(context).density).roundToInt()

private fun getDisplayMetrics(context: Context): DisplayMetrics =
    context.resources.displayMetrics

package com.teamtripdraw.android.support.framework.presentation

import java.time.format.DateTimeFormatter

object LocalDateTimeFormatter {
    val isoRemoveNanoSecondFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
}

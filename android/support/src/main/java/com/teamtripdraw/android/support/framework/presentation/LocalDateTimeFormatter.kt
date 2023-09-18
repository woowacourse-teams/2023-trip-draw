package com.teamtripdraw.android.support.framework.presentation

import java.time.format.DateTimeFormatter

object LocalDateTimeFormatter {
    val isoRemoveNanoSecondFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    val displayDateTimeFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy.MM.dd | HH:mm")
    val displayTripDateFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy.MM.dd")
}

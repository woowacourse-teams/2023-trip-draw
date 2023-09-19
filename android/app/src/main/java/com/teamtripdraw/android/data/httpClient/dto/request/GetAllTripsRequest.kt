package com.teamtripdraw.android.data.httpClient.dto.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetAllTripsRequest(
    @Json(name = "condition")
    val condition: TripCondition,
    @Json(name = "paging")
    val paging: Paging,
)

@JsonClass(generateAdapter = true)
data class TripCondition(
    @Json(name = "address")
    val address: String,
    @Json(name = "ageRanges")
    val ageRanges: List<Int>,
    @Json(name = "daysOfWeek")
    val daysOfWeek: List<Int>,
    @Json(name = "genders")
    val genders: List<Int>,
    @Json(name = "months")
    val months: List<Int>,
    @Json(name = "years")
    val years: List<Int>,
)

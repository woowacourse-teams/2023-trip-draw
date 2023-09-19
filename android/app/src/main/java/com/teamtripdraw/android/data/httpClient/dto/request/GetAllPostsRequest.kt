package com.teamtripdraw.android.data.httpClient.dto.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetAllPostsRequest(
    @Json(name = "condition")
    val condition: Condition,
    @Json(name = "paging")
    val paging: Paging,
)

@JsonClass(generateAdapter = true)
data class Condition(
    @Json(name = "address")
    val address: String,
    @Json(name = "ageRanges")
    val ageRanges: List<Int>,
    @Json(name = "currentPosition")
    val currentPosition: CurrentPosition,
    @Json(name = "daysOfWeek")
    val daysOfWeek: List<Int>,
    @Json(name = "genders")
    val genders: List<Int>,
    @Json(name = "hours")
    val hours: List<Int>,
    @Json(name = "months")
    val months: List<Int>,
    @Json(name = "years")
    val years: List<Int>,
)

@JsonClass(generateAdapter = true)
data class CurrentPosition(
    @Json(name = "latitude")
    val latitude: Double,
    @Json(name = "longitude")
    val longitude: Double,
)

@JsonClass(generateAdapter = true)
data class Paging(
    @Json(name = "lastViewedId")
    val lastViewedId: Int,
    @Json(name = "limit")
    val limit: Int,
)

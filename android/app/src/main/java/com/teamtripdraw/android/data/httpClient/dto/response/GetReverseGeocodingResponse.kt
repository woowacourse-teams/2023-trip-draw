package com.teamtripdraw.android.data.httpClient.dto.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetReverseGeocodingResponse(
    @Json(name = "results")
    val results: List<Result>,
    @Json(name = "status")
    val status: Status,
)

@JsonClass(generateAdapter = true)
data class Status(
    @Json(name = "code")
    val code: Int,
    @Json(name = "message")
    val message: String,
    @Json(name = "name")
    val name: String,
)

@JsonClass(generateAdapter = true)
data class Result(
    @Json(name = "code")
    val code: Code,
    @Json(name = "name")
    val name: String,
    @Json(name = "region")
    val region: Region,
)

@JsonClass(generateAdapter = true)
data class Region(
    @Json(name = "area0")
    val area0: Area,
    @Json(name = "area1")
    val area1: Area,
    @Json(name = "area2")
    val area2: Area,
    @Json(name = "area3")
    val area3: Area,
    @Json(name = "area4")
    val area4: Area,
)

@JsonClass(generateAdapter = true)
data class Area(
    @Json(name = "coords")
    val coords: Coords,
    @Json(name = "name")
    val name: String,
)

@JsonClass(generateAdapter = true)
data class Coords(
    @Json(name = "center")
    val center: Center,
)

@JsonClass(generateAdapter = true)
data class Center(
    @Json(name = "crs")
    val crs: String,
    @Json(name = "x")
    val x: Double,
    @Json(name = "y")
    val y: Double,
)

@JsonClass(generateAdapter = true)
data class Code(
    @Json(name = "id")
    val id: String,
    @Json(name = "mappingId")
    val mappingId: String,
    @Json(name = "type")
    val type: String,
)

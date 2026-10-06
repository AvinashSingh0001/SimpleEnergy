package com.example.simpleenergy.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VehicleDto(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "model") val model: String,
    @Json(name = "batteryPercent") val batteryPercent: Int,
    @Json(name = "rangeKm") val rangeKm: Int,
    @Json(name = "online") val online: Boolean,
    @Json(name = "speedKmh") val speedKmh: Int,
    @Json(name = "odometerKm") val odometerKm: Int,
    @Json(name = "lastUpdatedEpochMs") val lastUpdatedEpochMs: Long,
)

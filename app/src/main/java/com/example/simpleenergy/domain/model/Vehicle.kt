package com.example.simpleenergy.domain.model

data class Vehicle(
    val id: String,
    val name: String,
    val model: String,
    val batteryPercent: Int,
    val rangeKm: Int,
    val isOnline: Boolean,
)

data class VehicleDetail(
    val id: String,
    val name: String,
    val model: String,
    val batteryPercent: Int,
    val estimatedRangeKm: Int,
    val speedKmh: Int,
    val odometerKm: Int,
    val isOnline: Boolean,
    val lastUpdatedEpochMs: Long,
)

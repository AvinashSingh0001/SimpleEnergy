package com.example.simpleenergy.data.mapper

import com.example.simpleenergy.data.remote.dto.VehicleDto
import com.example.simpleenergy.domain.model.Vehicle
import com.example.simpleenergy.domain.model.VehicleDetail

fun VehicleDto.toVehicle(): Vehicle = Vehicle(
    id = id,
    name = name,
    model = model,
    batteryPercent = batteryPercent,
    rangeKm = rangeKm,
    isOnline = online,
)

fun VehicleDto.toVehicleDetail(): VehicleDetail = VehicleDetail(
    id = id,
    name = name,
    model = model,
    batteryPercent = batteryPercent,
    estimatedRangeKm = rangeKm,
    speedKmh = speedKmh,
    odometerKm = odometerKm,
    isOnline = online,
    lastUpdatedEpochMs = lastUpdatedEpochMs,
)

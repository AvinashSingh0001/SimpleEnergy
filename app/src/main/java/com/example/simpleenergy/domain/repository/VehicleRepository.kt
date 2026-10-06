package com.example.simpleenergy.domain.repository

import com.example.simpleenergy.domain.model.Vehicle
import com.example.simpleenergy.domain.model.VehicleDetail

interface VehicleRepository {
    suspend fun getVehicles(): Result<List<Vehicle>>
    suspend fun getVehicleDetail(vehicleId: String): Result<VehicleDetail>
}

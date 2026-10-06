package com.example.simpleenergy.domain.usecase

import com.example.simpleenergy.domain.model.Vehicle
import com.example.simpleenergy.domain.repository.VehicleRepository

class GetVehiclesUseCase(
    private val repository: VehicleRepository,
) {
    suspend operator fun invoke(): Result<List<Vehicle>> = repository.getVehicles()
}

package com.example.simpleenergy.domain.usecase

import com.example.simpleenergy.domain.model.VehicleDetail
import com.example.simpleenergy.domain.repository.VehicleRepository

class GetVehicleDetailUseCase(
    private val repository: VehicleRepository,
) {
    suspend operator fun invoke(vehicleId: String): Result<VehicleDetail> =
        repository.getVehicleDetail(vehicleId)
}

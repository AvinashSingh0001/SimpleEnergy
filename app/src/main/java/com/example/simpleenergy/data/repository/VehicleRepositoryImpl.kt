package com.example.simpleenergy.data.repository

import com.example.simpleenergy.data.mapper.toVehicle
import com.example.simpleenergy.data.mapper.toVehicleDetail
import com.example.simpleenergy.data.remote.VehicleApiService
import com.example.simpleenergy.domain.model.Vehicle
import com.example.simpleenergy.domain.model.VehicleDetail
import com.example.simpleenergy.domain.repository.VehicleRepository
import retrofit2.HttpException
import java.io.IOException

class VehicleRepositoryImpl(
    private val api: VehicleApiService,
) : VehicleRepository {

    override suspend fun getVehicles(): Result<List<Vehicle>> = runCatching {
        api.getVehicles().map { it.toVehicle() }
    }.mapFailure()

    override suspend fun getVehicleDetail(vehicleId: String): Result<VehicleDetail> = runCatching {
        if (vehicleId.isBlank()) {
            throw IllegalArgumentException("Vehicle id is required")
        }
        api.getVehicle(vehicleId).toVehicleDetail()
    }.mapFailure()

    private fun <T> Result<T>.mapFailure(): Result<T> = fold(
        onSuccess = { Result.success(it) },
        onFailure = { error ->
            Result.failure(mapThrowable(error))
        },
    )

    private fun mapThrowable(error: Throwable): Throwable = when (error) {
        is HttpException -> when (error.code()) {
            404 -> VehicleNotFoundException()
            in 500..599 -> ServerException()
            else -> ApiException("Request failed (${error.code()})")
        }
        is IOException -> NetworkException()
        is IllegalArgumentException -> error
        else -> error
    }
}

class NetworkException : IOException("Unable to reach the server. Check your connection and try again.")
class ServerException : IOException("The server is unavailable. Please try again later.")
class VehicleNotFoundException : IOException("Vehicle not found.")
class ApiException(message: String) : IOException(message)

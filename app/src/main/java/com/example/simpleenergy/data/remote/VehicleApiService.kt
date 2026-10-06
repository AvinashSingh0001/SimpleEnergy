package com.example.simpleenergy.data.remote

import com.example.simpleenergy.data.remote.dto.VehicleDto
import retrofit2.http.GET
import retrofit2.http.Path

interface VehicleApiService {
    @GET("vehicles")
    suspend fun getVehicles(): List<VehicleDto>

    @GET("vehicles/{id}")
    suspend fun getVehicle(@Path("id") id: String): VehicleDto
}

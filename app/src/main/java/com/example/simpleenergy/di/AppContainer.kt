package com.example.simpleenergy.di

import android.content.Context
import com.example.simpleenergy.data.remote.AssetMockInterceptor
import com.example.simpleenergy.data.remote.VehicleApiService
import com.example.simpleenergy.data.repository.VehicleRepositoryImpl
import com.example.simpleenergy.domain.repository.VehicleRepository
import com.example.simpleenergy.domain.usecase.GetVehicleDetailUseCase
import com.example.simpleenergy.domain.usecase.GetVehiclesUseCase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class AppContainer(context: Context) {

    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(AssetMockInterceptor(context.applicationContext))
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            },
        )
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    private val api: VehicleApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(VehicleApiService::class.java)

    val vehicleRepository: VehicleRepository = VehicleRepositoryImpl(api)

    val getVehiclesUseCase = GetVehiclesUseCase(vehicleRepository)
    val getVehicleDetailUseCase = GetVehicleDetailUseCase(vehicleRepository)

    companion object {
        const val BASE_URL = "https://mock.simpleenergy.local/"
    }
}

package com.example.simpleenergy.data.repository

import com.example.simpleenergy.data.remote.VehicleApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class VehicleRepositoryImplTest {

    private lateinit var server: MockWebServer
    private lateinit var repository: VehicleRepositoryImpl

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val api = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(VehicleApiService::class.java)
        repository = VehicleRepositoryImpl(api)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun getVehicles_mapsResponseToDomainModels() = runTest {
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(
                    """
                    [
                      {
                        "id": "v1",
                        "name": "Simple One",
                        "model": "Alpha",
                        "batteryPercent": 80,
                        "rangeKm": 100,
                        "online": true,
                        "speedKmh": 10,
                        "odometerKm": 1000,
                        "lastUpdatedEpochMs": 1
                      }
                    ]
                    """.trimIndent(),
                ),
        )

        val result = repository.getVehicles()

        assertTrue(result.isSuccess)
        val vehicles = result.getOrThrow()
        assertEquals(1, vehicles.size)
        assertEquals("Simple One", vehicles.first().name)
        assertEquals(100, vehicles.first().rangeKm)
        assertTrue(vehicles.first().isOnline)
    }

    @Test
    fun getVehicleDetail_returnsNotFoundFor404() = runTest {
        server.enqueue(MockResponse().setResponseCode(404))

        val result = repository.getVehicleDetail("missing-id")

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is VehicleNotFoundException)
    }
}

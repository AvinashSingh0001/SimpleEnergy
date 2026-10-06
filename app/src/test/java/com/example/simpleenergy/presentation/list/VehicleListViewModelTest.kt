package com.example.simpleenergy.presentation.list

import com.example.simpleenergy.domain.model.Vehicle
import com.example.simpleenergy.domain.usecase.GetVehiclesUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class VehicleListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getVehiclesUseCase: GetVehiclesUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getVehiclesUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadVehicles_emitsSuccessState() = runTest {
        val vehicles = listOf(
            Vehicle(
                id = "v1",
                name = "Simple One",
                model = "Alpha",
                batteryPercent = 70,
                rangeKm = 90,
                isOnline = true,
            ),
        )
        coEvery { getVehiclesUseCase() } returns Result.success(vehicles)

        val viewModel = VehicleListViewModel(getVehiclesUseCase)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(vehicles, state.vehicles)
        assertNull(state.errorMessage)
    }

    @Test
    fun loadVehicles_emitsErrorStateOnFailure() = runTest {
        coEvery { getVehiclesUseCase() } returns Result.failure(IllegalStateException("Network down"))

        val viewModel = VehicleListViewModel(getVehiclesUseCase)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Network down", state.errorMessage)
    }
}

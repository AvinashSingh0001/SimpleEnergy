package com.example.simpleenergy.presentation.list

import com.example.simpleenergy.domain.model.Vehicle

data class VehicleListUiState(
    val isLoading: Boolean = false,
    val vehicles: List<Vehicle> = emptyList(),
    val errorMessage: String? = null,
)

package com.example.simpleenergy.presentation.detail

import com.example.simpleenergy.domain.model.VehicleDetail

data class VehicleDetailUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val detail: VehicleDetail? = null,
    val errorMessage: String? = null,
)

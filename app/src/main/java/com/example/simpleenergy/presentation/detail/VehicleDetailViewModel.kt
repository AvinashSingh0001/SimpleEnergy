package com.example.simpleenergy.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.simpleenergy.domain.usecase.GetVehicleDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VehicleDetailViewModel(
    private val vehicleId: String,
    private val getVehicleDetailUseCase: GetVehicleDetailUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(VehicleDetailUiState(isLoading = true))
    val uiState: StateFlow<VehicleDetailUiState> = _uiState.asStateFlow()

    init {
        loadDetail(refresh = false)
    }

    fun refresh() {
        loadDetail(refresh = true)
    }

    private fun loadDetail(refresh: Boolean) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = !refresh && it.detail == null,
                    isRefreshing = refresh,
                    errorMessage = null,
                )
            }
            getVehicleDetailUseCase(vehicleId)
                .onSuccess { detail ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            detail = detail,
                            errorMessage = null,
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            errorMessage = error.message ?: "Failed to load vehicle details.",
                        )
                    }
                }
        }
    }

    class Factory(
        private val vehicleId: String,
        private val getVehicleDetailUseCase: GetVehicleDetailUseCase,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass.isAssignableFrom(VehicleDetailViewModel::class.java))
            return VehicleDetailViewModel(vehicleId, getVehicleDetailUseCase) as T
        }
    }
}

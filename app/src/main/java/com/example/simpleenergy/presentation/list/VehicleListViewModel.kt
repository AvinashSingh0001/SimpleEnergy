package com.example.simpleenergy.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.simpleenergy.domain.usecase.GetVehiclesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VehicleListViewModel(
    private val getVehiclesUseCase: GetVehiclesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(VehicleListUiState(isLoading = true))
    val uiState: StateFlow<VehicleListUiState> = _uiState.asStateFlow()

    init {
        loadVehicles()
    }

    fun loadVehicles() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            getVehiclesUseCase()
                .onSuccess { vehicles ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            vehicles = vehicles,
                            errorMessage = if (vehicles.isEmpty()) "No vehicles available." else null,
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Failed to load vehicles.",
                        )
                    }
                }
        }
    }

    class Factory(
        private val getVehiclesUseCase: GetVehiclesUseCase,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass.isAssignableFrom(VehicleListViewModel::class.java))
            return VehicleListViewModel(getVehiclesUseCase) as T
        }
    }
}

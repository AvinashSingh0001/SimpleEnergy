package com.example.simpleenergy.presentation.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.simpleenergy.domain.model.Vehicle
import com.example.simpleenergy.presentation.components.ErrorContent
import com.example.simpleenergy.presentation.components.LoadingContent
import com.example.simpleenergy.presentation.components.StatusBadge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleListScreen(
    viewModel: VehicleListViewModel,
    onVehicleClick: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Vehicles") })
        },
    ) { padding ->
        when {
            uiState.isLoading -> LoadingContent(Modifier.padding(padding))
            uiState.errorMessage != null && uiState.vehicles.isEmpty() -> {
                ErrorContent(
                    message = uiState.errorMessage.orEmpty(),
                    onRetry = viewModel::loadVehicles,
                    modifier = Modifier.padding(padding),
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    if (uiState.errorMessage != null) {
                        item {
                            Text(
                                text = uiState.errorMessage.orEmpty(),
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }
                    items(uiState.vehicles, key = { it.id }) { vehicle ->
                        VehicleListItem(
                            vehicle = vehicle,
                            onClick = { onVehicleClick(vehicle.id) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VehicleListItem(
    vehicle: Vehicle,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(text = vehicle.name, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = vehicle.model,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                StatusBadge(isOnline = vehicle.isOnline)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("Battery: ${vehicle.batteryPercent}%")
                Text("Range: ${vehicle.rangeKm} km")
            }
        }
    }
}

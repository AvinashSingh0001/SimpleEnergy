package com.example.simpleenergy.presentation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.simpleenergy.domain.model.VehicleDetail
import com.example.simpleenergy.presentation.components.ErrorContent
import com.example.simpleenergy.presentation.components.LoadingContent
import com.example.simpleenergy.presentation.components.StatusBadge
import java.text.DateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleDetailScreen(
    viewModel: VehicleDetailViewModel,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.detail?.name ?: "Vehicle Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = viewModel::refresh,
                        enabled = !uiState.isRefreshing,
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                },
            )
        },
    ) { padding ->
        when {
            uiState.isLoading -> LoadingContent(Modifier.padding(padding))
            uiState.errorMessage != null && uiState.detail == null -> {
                ErrorContent(
                    message = uiState.errorMessage.orEmpty(),
                    onRetry = viewModel::refresh,
                    modifier = Modifier.padding(padding),
                )
            }
            uiState.detail != null -> {
                DetailContent(
                    detail = uiState.detail!!,
                    errorMessage = uiState.errorMessage,
                    modifier = Modifier.padding(padding),
                )
            }
        }
    }
}

@Composable
private fun DetailContent(
    detail: VehicleDetail,
    errorMessage: String?,
    modifier: Modifier = Modifier,
) {
    val formattedTime = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
        .format(Date(detail.lastUpdatedEpochMs))

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(detail.name, style = MaterialTheme.typography.headlineSmall)
                Text(detail.model, style = MaterialTheme.typography.bodyLarge)
                StatusBadge(isOnline = detail.isOnline)
            }
        }
        MetricCard(title = "Battery", value = "${detail.batteryPercent}%")
        MetricCard(title = "Estimated range", value = "${detail.estimatedRangeKm} km")
        MetricCard(title = "Current speed", value = "${detail.speedKmh} km/h")
        MetricCard(title = "Odometer", value = "${detail.odometerKm} km")
        MetricCard(title = "Connectivity", value = if (detail.isOnline) "Connected" else "Disconnected")
        MetricCard(title = "Last updated", value = formattedTime)
    }
}

@Composable
private fun MetricCard(title: String, value: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 4.dp))
        }
    }
}

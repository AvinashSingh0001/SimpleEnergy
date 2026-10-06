package com.example.simpleenergy.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.simpleenergy.di.AppContainer
import com.example.simpleenergy.presentation.detail.VehicleDetailScreen
import com.example.simpleenergy.presentation.detail.VehicleDetailViewModel
import com.example.simpleenergy.presentation.list.VehicleListScreen
import com.example.simpleenergy.presentation.list.VehicleListViewModel

object Routes {
    const val VEHICLE_LIST = "vehicles"
    const val VEHICLE_DETAIL = "vehicles/{vehicleId}"

    fun vehicleDetail(vehicleId: String) = "vehicles/$vehicleId"
}

@Composable
fun AppNavigation(container: AppContainer) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.VEHICLE_LIST) {
        composable(Routes.VEHICLE_LIST) {
            val viewModel: VehicleListViewModel = viewModel(
                factory = VehicleListViewModel.Factory(container.getVehiclesUseCase),
            )
            VehicleListScreen(
                viewModel = viewModel,
                onVehicleClick = { id ->
                    navController.navigate(Routes.vehicleDetail(id))
                },
            )
        }
        composable(
            route = Routes.VEHICLE_DETAIL,
            arguments = listOf(navArgument("vehicleId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val vehicleId = backStackEntry.arguments?.getString("vehicleId").orEmpty()
            val viewModel: VehicleDetailViewModel = viewModel(
                factory = VehicleDetailViewModel.Factory(vehicleId, container.getVehicleDetailUseCase),
            )
            VehicleDetailScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
            )
        }
    }
}

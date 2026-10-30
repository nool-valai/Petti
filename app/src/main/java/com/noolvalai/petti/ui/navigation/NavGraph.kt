package com.noolvalai.petti.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.noolvalai.petti.ui.screens.grid.BentoGridScreen
import com.noolvalai.petti.ui.screens.grid.BentoGridViewModel
import com.noolvalai.petti.ui.screens.settings.SettingsScreen

object Destinations {
    const val SPLASH = "splash"
    const val GRID = "grid"
    const val SETTINGS = "settings"
}

@Composable
fun PettiNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Destinations.GRID,
    viewModel: BentoGridViewModel, // Accept the ViewModel parameter
    onHighContrastToggled: (Boolean) -> Unit // Accept the toggle callback
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Destinations.GRID) {
            BentoGridScreen(
                onNavigateToSettings = { navController.navigate(Destinations.SETTINGS) },
                viewModel = viewModel // Pass it to the grid screen
            )
        }

        composable(Destinations.SETTINGS) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onHighContrastToggled = onHighContrastToggled // Pass the callback down
            )
        }
    }
}
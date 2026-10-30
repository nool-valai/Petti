package com.noolvalai.petti

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.noolvalai.petti.ui.navigation.PettiNavGraph
import com.noolvalai.petti.ui.screens.grid.BentoGridUiState
import com.noolvalai.petti.ui.screens.grid.BentoGridViewModel
import com.noolvalai.petti.ui.theme.LocalHighContrast
import com.noolvalai.petti.ui.theme.PettiTheme
import com.noolvalai.petti.util.hasAllFilesAccess
import com.noolvalai.petti.util.hasUsageStatsPermission

class MainActivity : ComponentActivity() {

    // Instantiate the ViewModel at the Activity level
    private val viewModel: BentoGridViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("petti_settings", Context.MODE_PRIVATE)

        // Evaluate permissions before deciding to hold the splash screen
        val hasPermissions = hasAllFilesAccess() && hasUsageStatsPermission(this)

        splashScreen.setKeepOnScreenCondition {
                false
        }

        enableEdgeToEdge()

        setContent {
            // Read initial state from SharedPreferences
            var isHighContrast by remember {
                mutableStateOf(prefs.getBoolean("high_contrast", false))
            }

            // Provide the setting to the entire Compose tree
            CompositionLocalProvider(LocalHighContrast provides isHighContrast) {
                PettiTheme {
                    // Pass the Activity-scoped ViewModel down so the navigation graph uses this exact instance
                    PettiNavGraph(
                        viewModel = viewModel,
                        onHighContrastToggled = { enabled ->
                            isHighContrast = enabled
                            prefs.edit().putBoolean("high_contrast", enabled).apply()
                        }
                    )
                }
            }
        }
    }
}
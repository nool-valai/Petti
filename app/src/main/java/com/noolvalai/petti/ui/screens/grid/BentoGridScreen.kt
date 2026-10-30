package com.noolvalai.petti.ui.screens.grid

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings as SettingsIcon
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.noolvalai.petti.ui.components.BentoGrid
import com.noolvalai.petti.ui.components.SpinningPieChart
import com.noolvalai.petti.ui.theme.*
import com.noolvalai.petti.util.hasAllFilesAccess
import com.noolvalai.petti.util.hasNotificationPermission
import com.noolvalai.petti.util.hasUsageStatsPermission
import android.view.HapticFeedbackConstants
import androidx.compose.ui.platform.LocalView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BentoGridScreen(
    onNavigateToSettings: () -> Unit,
    viewModel: BentoGridViewModel = viewModel()
) {
    val context = LocalContext.current
    var hasFilesPermission by remember { mutableStateOf(hasAllFilesAccess()) }
    var hasUsagePermission by remember { mutableStateOf(hasUsageStatsPermission(context)) }
    var hasNotifPermission by remember { mutableStateOf(hasNotificationPermission(context)) }
    val view = LocalView.current

    // State to track if the user explicitly chose to skip the notification prompt
    var skipNotifications by remember { mutableStateOf(false) }

    val filesPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        hasFilesPermission = hasAllFilesAccess()
    }

    val usagePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        hasUsagePermission = hasUsageStatsPermission(context)
    }

    val notifPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasNotifPermission = isGranted
        if (!isGranted) skipNotifications = true
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Petti", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                },
                actions = {
                    IconButton(onClick = {
                        view.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
                        viewModel.analyzeStorage()
                    }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Rescan Storage")
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.SettingsIcon, contentDescription = "Settings")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        when {
            !hasFilesPermission -> {
                PermissionRationale(
                    title = "Storage Access Required",
                    description = "To accurately build your bento boxes, Petti needs permission to analyze your device's entire storage footprint.",
                    buttonText = "Grant Storage Access",
                    modifier = Modifier.padding(paddingValues)
                ) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                            data = Uri.parse("package:${context.packageName}")
                        }
                        filesPermissionLauncher.launch(intent)
                    }
                }
            }
            !hasUsagePermission -> {
                PermissionRationale(
                    title = "App Size Accuracy",
                    description = "To calculate the exact storage taken by your apps, Petti requires Usage Access.",
                    buttonText = "Grant Usage Access",
                    modifier = Modifier.padding(paddingValues)
                ) {
                    val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
                        data = Uri.parse("package:${context.packageName}")
                    }
                    usagePermissionLauncher.launch(intent)
                }
            }
            !hasNotifPermission && !skipNotifications && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                PermissionRationale(
                    title = "Background Alerts",
                    description = "Petti can scan your heavy storage in the background and notify you when your bento layout is ready.",
                    buttonText = "Allow Notifications",
                    secondaryButtonText = "Not Now",
                    onSecondaryRequest = { skipNotifications = true },
                    modifier = Modifier.padding(paddingValues)
                ) {
                    notifPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
            else -> {
                val uiState by viewModel.uiState.collectAsState()

                LaunchedEffect(Unit) {
                    viewModel.analyzeStorage()
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    when (val state = uiState) {
                        is BentoGridUiState.Loading -> {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                SpinningPieChart()
                                Spacer(modifier = Modifier.height(24.dp))
                                Text(
                                    text = state.message,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                        is BentoGridUiState.Success -> {
                            BentoGrid(categories = state.categories)
                        }
                        is BentoGridUiState.Error -> {
                            Text(
                                text = state.message,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PermissionRationale(
    title: String,
    description: String,
    buttonText: String,
    modifier: Modifier = Modifier,
    secondaryButtonText: String? = null,
    onSecondaryRequest: (() -> Unit)? = null,
    onRequest: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onRequest,
            shape = MaterialTheme.shapes.medium
        ) {
            Text(buttonText, fontWeight = FontWeight.Bold)
        }

        if (secondaryButtonText != null && onSecondaryRequest != null) {
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = onSecondaryRequest) {
                Text(
                    text = secondaryButtonText,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}
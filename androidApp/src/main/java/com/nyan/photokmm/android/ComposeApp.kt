package com.nyan.photokmm.android

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.nyan.photokmm.android.common.AppBar
import com.nyan.photokmm.android.common.Dashboard
import com.nyan.photokmm.android.common.photoDestinations
import com.nyan.photokmm.android.dashboard.DashboardScreen
import com.nyan.photokmm.android.dashboard.DashboardViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ComposeApp() {
    val navController = rememberNavController()
    val systemUiNavController = rememberSystemUiController()

    val isSystemDark = isSystemInDarkTheme()
    val statusBarColor = if (isSystemDark) {
        MaterialTheme.colorScheme.tertiary
    } else {
        Color.Transparent
    }
    SideEffect {
        systemUiNavController.setStatusBarColor(statusBarColor, darkIcons = !isSystemDark)
    }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = photoDestinations.find {
        backStackEntry?.destination?.route == it.route || backStackEntry?.destination?.route == it.routeWithArgs
    } ?: Dashboard

    Scaffold(
        topBar = {
            AppBar(
                canNavigateBack = navController.previousBackStackEntry != null,
                currentScreen = currentScreen
            ) {
                navController.navigateUp()
            }
        }
    ) { innerPaddings ->
        NavHost(
            navController = navController,
            modifier = Modifier.padding(innerPaddings),
            startDestination = Dashboard.routeWithArgs
        ) {
            composable(Dashboard.routeWithArgs) {
                val dashboardViewModel: DashboardViewModel = koinViewModel()
                DashboardScreen(
                    uiState = dashboardViewModel.uiState,
                    loadNextPhotos = {
                        dashboardViewModel.loadPhotos(forceReload = it)
                    },
                    navigateToDetail = {

                    })
            }
        }
    }
}
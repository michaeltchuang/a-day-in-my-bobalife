package com.michaeltchuang.example.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.michaeltchuang.example.ui.screens.CoinFlipperScreen
import com.michaeltchuang.example.ui.screens.HomeScreen
import com.michaeltchuang.example.ui.screens.ValidatorsListScreen

@Composable
fun BottomNavigationBar() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                bottomNavigationItems().forEachIndexed { _, navigationItem ->
                    NavigationBarItem(
                        selected = navigationItem.route == currentDestination?.route,
                        label = {
                            Text(navigationItem.label)
                        },
                        icon = {
                            Icon(
                                navigationItem.icon,
                                contentDescription = navigationItem.label,
                            )
                        },
                        onClick = {
                            navController.navigate(navigationItem.route) {
                                popUpTo(navController.graph.findStartDestination().navigatorName) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    )
                }
            }
        },
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues = paddingValues),
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    navController,
                )
            }
            composable(Screen.CoinFlipper.route) {
                CoinFlipperScreen(
                    navController,
                )
            }
            composable(Screen.ValidatorsList.route) {
                ValidatorsListScreen(
                    navController,
                )
            }
        }
    }
}

fun bottomNavigationItems(): List<BottomNavigationItem> {
    return listOf(
        BottomNavigationItem(
            label = "Home",
            icon = Icons.Filled.Home,
            route = Screen.Home.route,
        ),
        BottomNavigationItem(
            label = "Coin Flipper Game",
            icon = Icons.Filled.PlayArrow,
            route = Screen.CoinFlipper.route,
        ),
        BottomNavigationItem(
            label = "Search",
            icon = Icons.Filled.Search,
            route = Screen.ValidatorsList.route,
        ),
    )
}

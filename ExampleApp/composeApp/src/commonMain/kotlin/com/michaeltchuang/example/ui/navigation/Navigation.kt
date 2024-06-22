package com.michaeltchuang.example.ui.navigation

import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.michaeltchuang.example.ui.screens.AccountScreen
import com.michaeltchuang.example.ui.screens.HomeScreen
import com.michaeltchuang.example.ui.screens.LoginScreen
import com.michaeltchuang.example.ui.screens.PlayCoinFlipperScreen
import com.michaeltchuang.example.ui.screens.ValidatorDetailScreen
import com.michaeltchuang.example.ui.screens.ValidatorSearchScreen
import com.michaeltchuang.example.ui.viewmodels.AlgorandBaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.navigation.koinNavViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar()
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState, modifier = Modifier)
        },
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
            startDestination = Screen.HomeScreen.route,
            route = "parentRoute",
            modifier = Modifier.padding(paddingValues = paddingValues),
        ) {
            composable(Screen.HomeScreen.route) {
                val backStackEntry = remember(it) { navController.getBackStackEntry("parentRoute") }
                val sharedViewModel: AlgorandBaseViewModel = koinNavViewModel(viewModelStoreOwner = backStackEntry)
                HomeScreen(
                    tag = Screen.HomeScreen.route,
                    navController = navController,
                    algorandBaseViewModel = sharedViewModel,
                )
                SnackBarLayout(sharedViewModel, snackbarHostState)
            }
            composable(Screen.AccountScreen.route) {
                val backStackEntry = remember(it) { navController.getBackStackEntry("parentRoute") }
                val sharedViewModel: AlgorandBaseViewModel = koinNavViewModel(viewModelStoreOwner = backStackEntry)
                AccountScreen(
                    tag = Screen.AccountScreen.route,
                    navController = navController,
                    algorandBaseViewModel = sharedViewModel,
                )
                SnackBarLayout(sharedViewModel, snackbarHostState)
            }
            composable(Screen.LoginScreen.route) {
                val backStackEntry = remember(it) { navController.getBackStackEntry("parentRoute") }
                val sharedViewModel: AlgorandBaseViewModel = koinNavViewModel(viewModelStoreOwner = backStackEntry)
                LoginScreen(
                    tag = Screen.LoginScreen.route,
                    navController = navController,
                    algorandBaseViewModel = sharedViewModel,
                )
                SnackBarLayout(sharedViewModel, snackbarHostState)
            }
            composable(Screen.PlayCoinFlipperScreen.route) {
                val backStackEntry = remember(it) { navController.getBackStackEntry("parentRoute") }
                val sharedViewModel: AlgorandBaseViewModel = koinNavViewModel(viewModelStoreOwner = backStackEntry)
                PlayCoinFlipperScreen(
                    tag = Screen.PlayCoinFlipperScreen.route,
                    navController = navController,
                    algorandBaseViewModel = sharedViewModel,
                )
                SnackBarLayout(sharedViewModel, snackbarHostState)
            }
            composable(Screen.ValidatorSearchScreen.route) {
                val backStackEntry = remember(it) { navController.getBackStackEntry("parentRoute") }
                val sharedViewModel: AlgorandBaseViewModel = koinNavViewModel(viewModelStoreOwner = backStackEntry)
                ValidatorSearchScreen(
                    tag = Screen.ValidatorSearchScreen.route,
                    navController = navController,
                    algorandBaseViewModel = sharedViewModel,
                )
                SnackBarLayout(sharedViewModel, snackbarHostState)
            }
            composable(Screen.ValidatorDetailScreen.route + "/{validatorId}",
                arguments = listOf(navArgument("validatorId") { type = NavType.IntType })
            ) {
                val backStackEntry = remember(it) { navController.getBackStackEntry("parentRoute") }
                val sharedViewModel: AlgorandBaseViewModel = koinNavViewModel(viewModelStoreOwner = backStackEntry)
                ValidatorDetailScreen(
                    tag = Screen.ValidatorDetailScreen.route,
                    navController = navController,
                    algorandBaseViewModel = sharedViewModel,
                    validatorId = (it.arguments as Bundle).get("validatorId") as Int
                )
                SnackBarLayout(sharedViewModel, snackbarHostState)
            }
        }
    }
}

@Composable
fun SnackBarLayout(
    algorandBaseViewModel: AlgorandBaseViewModel,
    snackbarHostState: SnackbarHostState,
) {
    val scope = rememberCoroutineScope()
    val snackBarStateFlow by algorandBaseViewModel.snackBarStateFlow.collectAsStateWithLifecycle()
    if (snackBarStateFlow.trim().length > 0) {
        // val context = LocalContext.current
        LaunchedEffect(Unit) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    duration = SnackbarDuration.Short,
                    message = snackBarStateFlow,
                )
                // Toast.makeText(context, snackBarStateFlow, Toast.LENGTH_SHORT).show()
                launch {
                    delay(1000L)
                    algorandBaseViewModel.setSnackBarMessage("")
                }
            }
        }
    }
}

fun bottomNavigationItems(): List<BottomNavigationItem> =
    listOf(
        BottomNavigationItem(
            label = "Home",
            icon = Icons.Filled.Home,
            route = Screen.HomeScreen.route,
        ),
        BottomNavigationItem(
            label = "Algorand Experience",
            icon = Icons.Filled.ShoppingCart,
            route = Screen.LoginScreen.route,
        ),
    )

data class BottomNavigationItem(
    val label: String = "",
    val icon: ImageVector = Icons.Filled.Home,
    val route: String = "",
)

sealed class Screen(
    val route: String,
) {
    object HomeScreen : Screen("home")

    object AccountScreen : Screen("account")

    object LoginScreen : Screen("algorand_experience")

    object PlayCoinFlipperScreen : Screen("play_coin_flipper")

    object ValidatorDetailScreen : Screen("validator_detail")

    object ValidatorSearchScreen : Screen("validator_search")
}

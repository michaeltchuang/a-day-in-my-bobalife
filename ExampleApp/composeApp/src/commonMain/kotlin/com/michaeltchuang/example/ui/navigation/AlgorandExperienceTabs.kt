package com.michaeltchuang.example.ui.navigation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.michaeltchuang.example.ui.screens.AccountScreen
import com.michaeltchuang.example.ui.screens.DispenserScreen
import com.michaeltchuang.example.ui.screens.PlayCoinFlipperScreen
import com.michaeltchuang.example.ui.theme.md_theme_light_primary
import com.michaeltchuang.example.ui.viewmodels.AlgorandBaseViewModel
import com.russhwolf.settings.BuildConfig
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AlgorandExperienceTabs(
    navController: NavController,
    algorandBaseViewModel: AlgorandBaseViewModel,
) {
    val accountStateFlow by algorandBaseViewModel.accountStateFlow.collectAsStateWithLifecycle()
    if (algorandBaseViewModel.account == null || accountStateFlow == null) {
        Log.d(navController.currentDestination?.route, "No account detected")
        navController.popBackStack(Screen.LoginScreen.route, false)
    }

    val tabData = getTabList()
    val pagerState = rememberPagerState(pageCount = { tabData.size })
    Column(modifier = Modifier.fillMaxSize()) {
        TabLayout(tabData, pagerState)
        TabContent(pagerState, navController, algorandBaseViewModel)
    }
}

@Composable
fun TabLayout(
    tabData: List<Pair<String, ImageVector>>,
    pagerState: PagerState,
) {
    val scope = rememberCoroutineScope()
    /*     val tabColor = listOf(
             Color.Gray,
             Color.Yellow,
             Color.Blue,
             Color.Red,
             Color.Green
         )*/
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        divider = {
            Spacer(modifier = Modifier.height(5.dp))
        },
        indicator = { tabPositions ->
            SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                height = 3.dp,
                color = md_theme_light_primary,
            )
        },
        modifier =
            Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
    ) {
        tabData.forEachIndexed { index, data ->
            LeadingIconTab(
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                icon = {
                    Icon(imageVector = data.second, contentDescription = null)
                },
                text = {
                    Text(text = data.first, fontSize = 12.sp)
                },
            )
        }
    }
}

@Composable
fun TabContent(
    pagerState: PagerState,
    navController: NavController,
    algorandBaseViewModel: AlgorandBaseViewModel,
) {
    HorizontalPager(state = pagerState) { index ->
        when (index) {
            0 -> {
                // navController.navigate(Screen.AccountScreen.route)
                AccountScreen(
                    navController = navController,
                    algorandBaseViewModel = algorandBaseViewModel,
                    tag = Screen.AccountScreen.route,
                )
            }
            1 -> {
                // navController.navigate(Screen.ValidatorSearchScreen.route)
                DispenserScreen(
                    navController = navController,
                    algorandBaseViewModel = algorandBaseViewModel,
                    tag = Screen.DispenserScreen.route,
                )
            }
        }
        if (BuildConfig.DEBUG) {
            when (index) {
                2 -> {
                    // navController.navigate(Screen.PlayCoinFlipperScreen.route)
                    PlayCoinFlipperScreen(
                        navController = navController,
                        algorandBaseViewModel = algorandBaseViewModel,
                        tag = Screen.PlayCoinFlipperScreen.route,
                    )
                }
            }
        }
    }
}

private fun getTabList(): List<Pair<String, ImageVector>> =
    if (BuildConfig.DEBUG) {
        listOf(
            "Account" to Icons.Default.AccountCircle,
            "Dispenser" to Icons.Default.ShoppingCart,
            "Coin Flipper" to Icons.Default.PlayArrow,
        )
    } else {
        listOf(
            "Account" to Icons.Default.AccountCircle,
            "Dispenser" to Icons.Default.ShoppingCart,
        )
    }

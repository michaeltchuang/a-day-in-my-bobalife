package com.michaeltchuang.example.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.michaeltchuang.example.ui.navigation.Screen
import com.michaeltchuang.example.ui.viewmodels.AlgorandBaseViewModel
import com.michaeltchuang.example.ui.viewmodels.ValidatorsListViewModel
import com.michaeltchuang.example.ui.widgets.ValidatorSearchWidget
import com.michaeltchuang.example.utils.getJsonDataFromAsset
import org.koin.compose.getKoin
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@SuppressLint("ComposableNaming")
@Composable
fun ValidatorDetailScreen(
    navController: NavController,
    algorandBaseViewModel: AlgorandBaseViewModel,
    validatorId: Int = 1,
    tag: String,
) {
    val accountStateFlow by algorandBaseViewModel.accountStateFlow.collectAsStateWithLifecycle()
    if (algorandBaseViewModel.account == null || accountStateFlow == null) {
        Log.d(tag, "No account detected")
        navController.popBackStack(Screen.LoginScreen.route, false)
    }

    val validatorListViewModel: ValidatorsListViewModel = getKoin().get()
    // val screenState by validatorListViewModel.validatorsListViewState.collectAsState()
    validatorListViewModel.algorandBaseViewModel = algorandBaseViewModel
    // validatorListViewModel.context = WeakReference(LocalContext.current)
    validatorListViewModel.abiContract = getJsonDataFromAsset(
        LocalContext.current,
        "ValidatorRegistry.arc4.json",
    ) ?: ""

    ValidatorSearchWidget(
        onValidatorSelected = { validator ->
            // navController.navigate(Screen.PlayerDetailsScreen.title + "/${player.id}")
        },
    )
}

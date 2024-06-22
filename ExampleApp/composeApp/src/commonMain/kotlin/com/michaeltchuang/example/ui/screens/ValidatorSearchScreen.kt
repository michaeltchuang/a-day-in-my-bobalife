package com.michaeltchuang.example.ui.screens

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
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
fun ValidatorSearchScreen(
    navController: NavController,
    algorandBaseViewModel: AlgorandBaseViewModel,
    tag: String,
) {
    // AlgorandExperienceTabs(navController, algorandBaseViewModel)

    val validatorListViewModel: ValidatorsListViewModel = getKoin().get()
    // val screenState by validatorListViewModel.validatorsListViewState.collectAsState()
    validatorListViewModel.algorandBaseViewModel = algorandBaseViewModel
    // validatorListViewModel.context = WeakReference(LocalContext.current)
    validatorListViewModel.abiContract = getJsonDataFromAsset(
        LocalContext.current,
        "ValidatorRegistry.arc4.json",
    ) ?: ""
    validatorListViewModel.setupDB()

    ValidatorSearchWidget(
        onValidatorSelected = { validatorId ->
            navController.navigate(Screen.ValidatorDetailScreen.route + "/${validatorId}")
        },
    )
}

package com.michaeltchuang.example.ui.screens

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.michaeltchuang.example.ui.viewmodels.BaseViewModel
import com.michaeltchuang.example.ui.viewmodels.ValidatorsListViewModel
import com.michaeltchuang.example.ui.widgets.SearchableToolbar
import org.koin.compose.getKoin
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun ValidatorsListScreen() {
    val TAG = "ValidatorsListScreen"

    val activityViewModel: BaseViewModel =
        koinViewModel(
            viewModelStoreOwner = LocalContext.current as ComponentActivity,
        )
    if (activityViewModel.account == null) {
        Log.d(TAG, "No account detected")
        null.also { activityViewModel._account.value = it }
    }

    val validatorListViewModel: ValidatorsListViewModel = getKoin().get()
    // val screenState by validatorListViewModel.validatorsListViewState.collectAsState()
    validatorListViewModel.account = activityViewModel.account
    // validatorListViewModel.context = WeakReference(LocalContext.current)
    validatorListViewModel.abiContract = getJsonDataFromAsset(
        LocalContext.current, "ValidatorRegistry.arc4.json",
    ) ?: ""

    LaunchedEffect(Unit) {
        validatorListViewModel.insertValidatorIntoDb()
        // validatorListViewModel.fetchValidatorCount()
    }

    SearchableToolbar(
        onPlayerSelected = { player ->
            // navController.navigate(Screen.PlayerDetailsScreen.title + "/${player.id}")
        },
        onShowSettings = {
            // navController.navigate(Screen.SettingsScreen.title)
        },
    )

//    when (screenState) {
//        is ScreenState .Loading -> {
//            PiProgressIndicator()
//        }
//        is ScreenState.Success -> {
//            val validators = (screenState as ScreenState.Success)
//                .responseData
//            ValidatorCards(validators)
//        }
//        is ScreenState.Error -> {
//            // show Error
//        }
//    }
}

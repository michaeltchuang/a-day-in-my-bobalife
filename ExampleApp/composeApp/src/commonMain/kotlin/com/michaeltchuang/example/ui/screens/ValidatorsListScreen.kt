package com.michaeltchuang.example.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.michaeltchuang.example.ui.viewmodels.BaseViewModel
import com.michaeltchuang.example.ui.viewmodels.ValidatorsListViewModel
import com.michaeltchuang.example.ui.widgets.PiProgressIndicator
import com.michaeltchuang.example.ui.widgets.SearchableToolbar
import com.michaeltchuang.example.ui.widgets.ValidatorCards
import org.koin.compose.getKoin

@Composable
fun ValidatorsListScreen(activityViewModel: BaseViewModel) {
    SearchableToolbar()

    val validatorListViewModel: ValidatorsListViewModel = getKoin().get()
    val screenState by validatorListViewModel.validatorsListViewState.collectAsState()
    validatorListViewModel.abiContract = getJsonDataFromAsset(
        LocalContext.current, "ValidatorRegistry.arc4.json",
    ) ?: ""
    validatorListViewModel.account = activityViewModel.account
    validatorListViewModel.abiContract = getJsonDataFromAsset(
        LocalContext.current, "ValidatorRegistry.arc4.json",
    ) ?: ""

    LaunchedEffect(Unit) {
        validatorListViewModel.getValidators()
        validatorListViewModel.fetchValidatorCount()
    }
    when (screenState) {
        is ValidatorsListViewModel.ValidatorsListScreenState .Loading -> {
            PiProgressIndicator()
        }
        is ValidatorsListViewModel.ValidatorsListScreenState.Success -> {
            val validators = (screenState as ValidatorsListViewModel.ValidatorsListScreenState.Success)
                .responseData
            ValidatorCards(validators)
        }
        is ValidatorsListViewModel.ValidatorsListScreenState.Error -> {
            // show Error
        }
    }
}

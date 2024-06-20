package com.michaeltchuang.example.ui.screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.michaeltchuang.example.ui.navigation.Screen
import com.michaeltchuang.example.ui.viewmodels.AlgorandBaseViewModel
import com.michaeltchuang.example.ui.widgets.AlgorandButton
import com.michaeltchuang.example.ui.widgets.PassphraseField
import com.michaeltchuang.example.utils.Constants
import example_app.composeapp.generated.resources.Res
import example_app.composeapp.generated.resources.account_address
import example_app.composeapp.generated.resources.account_button_lock
import example_app.composeapp.generated.resources.account_passphrase
import org.jetbrains.compose.resources.stringResource
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@SuppressLint("ComposableNaming")
@Composable
fun AccountScreen(
    navController: NavController,
    algorandBaseViewModel: AlgorandBaseViewModel,
    tag: String,
) {
    // AlgorandExperienceTabs(navController, algorandBaseViewModel)
    BackHandler {
        navController.popBackStack(Screen.HomeScreen.route, false)
    }
    algorandBaseViewModel.appOptInStateCheck(algorandBaseViewModel.account!!, Constants.COINFLIP_APP_ID_TESTNET)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier =
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.White),
    ) {
        PassphraseField(stringResource(resource = Res.string.account_address), algorandBaseViewModel.account?.address.toString())
        PassphraseField(stringResource(resource = Res.string.account_passphrase), algorandBaseViewModel.account?.toMnemonic().toString())
        AlgorandButton(
            stringResourceId = Res.string.account_button_lock,
            onClick = {
                algorandBaseViewModel._account.value = null
            },
        )
    }
}

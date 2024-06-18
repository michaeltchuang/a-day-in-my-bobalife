package com.michaeltchuang.example.ui.screens

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.michaeltchuang.example.ui.viewmodels.AlgorandBaseViewModel
import com.michaeltchuang.example.ui.widgets.AlgorandButton
import com.michaeltchuang.example.ui.widgets.PassphraseField
import com.michaeltchuang.example.utils.Constants
import example_app.composeapp.generated.resources.Res
import example_app.composeapp.generated.resources.account_address
import example_app.composeapp.generated.resources.account_button_lock
import example_app.composeapp.generated.resources.account_passphrase
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun AccountScreen() {
    val TAG = "AccountScreen"

    val algorandBaseViewModel: AlgorandBaseViewModel =
        koinViewModel(
            viewModelStoreOwner = LocalContext.current as ComponentActivity,
        )
    if (algorandBaseViewModel.account == null) {
        Log.d(TAG, "No account detected")
        null.also { algorandBaseViewModel._account.value = it }
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

package com.michaeltchuang.example.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.michaeltchuang.example.ui.viewmodels.BaseViewModel
import com.michaeltchuang.example.ui.widgets.AlgorandButton
import com.michaeltchuang.example.ui.widgets.PassphraseField
import example_app.composeapp.generated.resources.Res
import example_app.composeapp.generated.resources.account_address
import example_app.composeapp.generated.resources.account_button_lock
import example_app.composeapp.generated.resources.account_passphrase
import org.jetbrains.compose.resources.stringResource

@Composable
fun AccountScreen(activityViewModel: BaseViewModel) {
    val TAG = "AccountScreen"
    val account by activityViewModel.accountStateFlow.collectAsStateWithLifecycle()
    if (account == null) {
        Log.d(TAG, "No account detected")
        null.also { activityViewModel._account.value = it }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier =
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.White),
    ) {
        PassphraseField(stringResource(resource = Res.string.account_address), account?.address.toString())
        PassphraseField(stringResource(resource = Res.string.account_passphrase), account?.toMnemonic().toString())
        AlgorandButton(
            stringResourceId = Res.string.account_button_lock,
            onClick = {
                activityViewModel._account.value = null
            },
        )
    }
}

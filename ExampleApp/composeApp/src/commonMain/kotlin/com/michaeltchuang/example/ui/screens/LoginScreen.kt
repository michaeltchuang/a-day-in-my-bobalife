package com.michaeltchuang.example.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.michaeltchuang.example.ui.navigation.AlgorandExperienceTabs
import com.michaeltchuang.example.ui.viewmodels.AlgorandBaseViewModel
import com.michaeltchuang.example.ui.widgets.AlgorandButton
import com.michaeltchuang.example.ui.widgets.AlgorandDivider
import com.michaeltchuang.example.ui.widgets.PassphraseField
import com.michaeltchuang.example.ui.widgets.passphraseTextField
import example_app.composeapp.generated.resources.Res
import example_app.composeapp.generated.resources.coin_heads
import example_app.composeapp.generated.resources.login_button_create
import example_app.composeapp.generated.resources.login_button_restore
import example_app.composeapp.generated.resources.login_disclaimer
import example_app.composeapp.generated.resources.login_guest_message
import example_app.composeapp.generated.resources.login_restore_textfield_value
import example_app.composeapp.generated.resources.login_restore_textifield_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@SuppressLint("ComposableNaming")
@Composable
fun LoginScreen(
    navController: NavController,
    algorandBaseViewModel: AlgorandBaseViewModel,
    tag: String,
) {
    val account by algorandBaseViewModel.accountStateFlow.collectAsStateWithLifecycle()
    if (account != null) {
        // already logged in
        AlgorandExperienceTabs(navController, algorandBaseViewModel)
        // navController.navigate(Screen.AccountScreen.route)
        return
    } else {
        // showSnackbar(stringResource(resource = Res.string.error_account))
    }

    val label = Res.string.login_restore_textifield_title
    val dataInput = Res.string.login_restore_textfield_value
    passphraseTextField = dataInput.toString()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier =
            Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
    ) {
        Text(
            stringResource(resource = Res.string.login_guest_message),
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            style =
                TextStyle(
                    fontSize = 24.sp,
                ),
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center,
            modifier =
                Modifier
                    .width(300.dp)
                    .wrapContentHeight(),
        )
        Image(
            painter = painterResource(Res.drawable.coin_heads),
            contentDescription = Res.string.login_guest_message.toString(),
            modifier =
                Modifier
                    .wrapContentSize()
                    .wrapContentHeight()
                    .wrapContentWidth()
                    .size(100.dp),
        )
        AlgorandDivider()
        AlgorandButton(stringResourceId = Res.string.login_button_create, onClick = {
            algorandBaseViewModel.createAccount()
        })
        AlgorandDivider()
        PassphraseField(stringResource(resource = label), stringResource(resource = dataInput))
        AlgorandButton(stringResourceId = Res.string.login_button_restore, onClick = {
            algorandBaseViewModel.recoverAccount(passphraseTextField, true)
            // algorandBaseViewModel.recoverAccount("${Constants.TEST_PASSPHRASE_PART1} ${Constants.TEST_PASSPHRASE_PART2}", false)
        })
        AlgorandDivider()
        Text(
            stringResource(resource = Res.string.login_disclaimer),
            color = Color.Black,
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center,
            modifier =
                Modifier
                    .width(262.dp)
                    .wrapContentHeight()
                    .alpha(0.6F),
        )
    }
}

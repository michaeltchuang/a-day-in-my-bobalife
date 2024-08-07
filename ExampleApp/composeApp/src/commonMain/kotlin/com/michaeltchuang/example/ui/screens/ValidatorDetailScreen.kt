package com.michaeltchuang.example.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.michaeltchuang.example.data.local.entities.ValidatorEntity
import com.michaeltchuang.example.ui.navigation.Screen
import com.michaeltchuang.example.ui.theme.md_theme_light_onPrimaryContainer
import com.michaeltchuang.example.ui.theme.md_theme_light_outline
import com.michaeltchuang.example.ui.theme.md_theme_light_secondary
import com.michaeltchuang.example.ui.theme.md_theme_light_tab
import com.michaeltchuang.example.ui.viewmodels.AlgorandBaseViewModel
import com.michaeltchuang.example.ui.viewmodels.ValidatorDetailUIState
import com.michaeltchuang.example.ui.viewmodels.ValidatorDetailViewModel
import com.michaeltchuang.example.ui.widgets.AlgorandButton
import com.michaeltchuang.example.utils.truncatedAlgorandAddress
import example_app.composeapp.generated.resources.Res
import example_app.composeapp.generated.resources.loading
import example_app.composeapp.generated.resources.validator_detail_button_wait_message
import example_app.composeapp.generated.resources.validator_detail_confirm_stake
import example_app.composeapp.generated.resources.validator_detail_label_account
import example_app.composeapp.generated.resources.validator_detail_label_apy
import example_app.composeapp.generated.resources.validator_detail_label_fee
import example_app.composeapp.generated.resources.validator_detail_label_validator
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.getKoin
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@SuppressLint("ComposableNaming")
@Composable
fun ValidatorDetailScreen(
    navController: NavController,
    algorandBaseViewModel: AlgorandBaseViewModel,
    validatorId: Int = 0,
    tag: String,
) {

    val viewModel: ValidatorDetailViewModel = getKoin().get()
    viewModel.algorandBaseViewModel = algorandBaseViewModel

    val validatorDetailUIState = viewModel.validatorDetailUIState.collectAsStateWithLifecycle()
    viewModel.fetchValidatorById(validatorId)

    when (val uiState = validatorDetailUIState.value) {
        is ValidatorDetailUIState.Error -> {
            navController.navigate(Screen.ValidatorSearchScreen.route)
            algorandBaseViewModel.setSnackBarMessage(uiState.message)
        }

        is ValidatorDetailUIState.Loading -> {
            // show placeholder UI
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier =
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
            ) {
                Text(
                    fontWeight = FontWeight.Bold,
                    text = stringResource(resource = Res.string.loading),
                )
            }
        }

        is ValidatorDetailUIState.Success -> {
            Content(
                viewModel = viewModel,
                algorandBaseViewModel = algorandBaseViewModel,
                validator = uiState.result)
        }
    }
}

@Composable
fun Content(
    validator: ValidatorEntity,
    algorandBaseViewModel: AlgorandBaseViewModel,
    viewModel: ValidatorDetailViewModel)
{
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        Spacer(modifier = Modifier.height(150.dp))
        Text(
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            text = "Stake 5 ALGO",
        )
        Text(
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = md_theme_light_outline,
            text = "$1.00",
        )

        Spacer(modifier = Modifier.height(50.dp))

        val formattedAddress = truncatedAlgorandAddress(viewModel.algorandBaseViewModel?.account?.address.toString())

        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(start = 50.dp, end = 50.dp)
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            ValidatorDetailRowItem(
                label = stringResource(resource = Res.string.validator_detail_label_account),
                value = formattedAddress,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            ValidatorDetailRowItem(
                label = stringResource(resource = Res.string.validator_detail_label_validator),
                value = validator.name,
                shape = RoundedCornerShape(0.dp))
            ValidatorDetailRowItem(
                stringResource(resource = Res.string.validator_detail_label_apy),
                value = "${validator.apy}%",
                shape = RoundedCornerShape(0.dp))
            ValidatorDetailRowItem(
                stringResource(resource = Res.string.validator_detail_label_fee),
                value = "${validator.percentToValidator?.div(10000)}%",
                shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
        }

        Spacer(modifier = Modifier.weight(1F))

        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center,
            modifier =
            Modifier
                .padding(50.dp)
                .height(300.dp)
                .fillMaxWidth(),
        ) {
            AlgorandButton(
                stringResourceId = Res.string.validator_detail_confirm_stake,
                onClick = {
                    viewModel.fetchValidatorById(
                        validator.id
                    )
                    algorandBaseViewModel.setSnackBarMessage(Res.string.validator_detail_button_wait_message)
                },
            )
        }
    }
}

@Composable
fun ValidatorDetailRowItem(label: String, value: String, shape: RoundedCornerShape) {
    Spacer(Modifier.height(2.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier =
        Modifier
            .background(color = md_theme_light_tab, shape = shape)
            .padding(10.dp)
            .height(50.dp)
            .fillMaxWidth(),
    ) {
        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier =
            Modifier
                .padding(start = 15.dp, end = 15.dp)
                .wrapContentWidth()
                .fillMaxHeight(),
        ) {
            Text(
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = md_theme_light_secondary,
                text = label,
            )
        }
        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End,
            modifier =
            Modifier
                .padding(start = 15.dp, end = 15.dp)
                .weight(1f)
                .fillMaxHeight(),
        ) {
            Text(
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = md_theme_light_onPrimaryContainer,
                text = value,
            )
        }
    }
}

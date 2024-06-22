package com.michaeltchuang.example.ui.widgets

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.michaeltchuang.example.data.local.entities.ValidatorEntity
import com.michaeltchuang.example.ui.theme.md_theme_light_onPrimary
import com.michaeltchuang.example.ui.theme.md_theme_light_primary
import com.michaeltchuang.example.ui.viewmodels.ValidatorsListUIState
import com.michaeltchuang.example.ui.viewmodels.ValidatorsListViewModel
import example_app.composeapp.generated.resources.Res
import example_app.composeapp.generated.resources.loading
import example_app.composeapp.generated.resources.validator_est_apy
import example_app.composeapp.generated.resources.validator_est_apy_desc
import example_app.composeapp.generated.resources.validator_est_apy_long
import example_app.composeapp.generated.resources.validator_label
import example_app.composeapp.generated.resources.validator_label_desc
import example_app.composeapp.generated.resources.validator_no_results
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class, ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@SuppressLint("ComposableNaming")
@Composable
fun ValidatorSearchWidget(onValidatorSelected: (validatorId: Int) -> Unit) {
    val validatorsListViewModel = koinViewModel<ValidatorsListViewModel>()
    val validatorListUIState = validatorsListViewModel.validatorsListUIState.collectAsStateWithLifecycle()

    val allValidators = validatorsListViewModel.validatorsFromDb.collectAsStateWithLifecycle()
    val validatorSearchQuery = validatorsListViewModel.searchQuery.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(ValidatorBottomSheeet.HIDE) }
    var showNoResults by remember { mutableStateOf(false) }

    Column(
        modifier =
            Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
    ) {
        val isDataLoading = allValidators.value.isEmpty()
        TextField(
            singleLine = true,
            value = validatorSearchQuery.value,
            modifier =
                Modifier
                    .fillMaxWidth(),
            label = {
                Text(text = "Search")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                )
            },
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search,
                ),
            onValueChange = { searchQuery ->
                validatorsListViewModel.onValidatorSearchQueryChange(searchQuery)
            },
            trailingIcon = {
                if (validatorSearchQuery.value.isNotEmpty()) {
                    Icon(
                        modifier =
                            Modifier.clickable {
                                validatorsListViewModel.onValidatorSearchQueryChange("")
                            },
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear search",
                    )
                }
            },
        )

        when (val uiState = validatorListUIState.value) {
            is ValidatorsListUIState.Error -> {
                Text("Error: ${uiState.message}")
            }

            is ValidatorsListUIState.Loading -> {
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

            is ValidatorsListUIState.Success -> {
                LazyColumn {
                    stickyHeader {
                        Row(
                            modifier =
                                Modifier
                                    .background(color = md_theme_light_onPrimary)
                                    .wrapContentHeight()
                                    .fillMaxWidth(),
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.Start,
                                modifier =
                                    Modifier
                                        .padding(start = 10.dp)
                                        .fillMaxHeight()
                                        .weight(1f),
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start,
                                    modifier =
                                        Modifier
                                            .padding(15.dp)
                                            .background(color = md_theme_light_onPrimary)
                                            .wrapContentHeight()
                                            .fillMaxWidth(),
                                ) {
                                    Text(
                                        fontWeight = FontWeight.Bold,
                                        color = md_theme_light_primary,
                                        text = stringResource(resource = Res.string.validator_label),
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Icon(
                                        Icons.Default.Info,
                                        contentDescription = stringResource(resource = Res.string.validator_label),
                                        tint = md_theme_light_primary,
                                        modifier = Modifier.clickable { showBottomSheet = ValidatorBottomSheeet.VALIDATOR },
                                    )
                                }
                            }
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.End,
                                modifier =
                                    Modifier
                                        .padding(end = 10.dp)
                                        .fillMaxHeight()
                                        .weight(1f),
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.End,
                                    modifier =
                                        Modifier
                                            .padding(15.dp)
                                            .background(color = md_theme_light_onPrimary)
                                            .wrapContentHeight()
                                            .fillMaxWidth(),
                                ) {
                                    Text(
                                        fontWeight = FontWeight.Bold,
                                        color = md_theme_light_primary,
                                        text = stringResource(resource = Res.string.validator_est_apy),
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Icon(
                                        Icons.Default.Info,
                                        contentDescription = stringResource(resource = Res.string.validator_est_apy),
                                        tint = md_theme_light_primary,
                                        modifier = Modifier.clickable { showBottomSheet = ValidatorBottomSheeet.APY },
                                    )
                                }
                            }
                        }
                    }

                    showNoResults = uiState.result.isEmpty()
                    items(
                        items = uiState.result,
                        itemContent = { validator ->
                            ValidatorSearchListItem(validator, onValidatorSelected)
                        },
                    )
                }

                if (showNoResults) {
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
                            text = stringResource(resource = Res.string.validator_no_results),
                        )
                    }
                }
            }
        }

        if (showBottomSheet != ValidatorBottomSheeet.HIDE) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = ValidatorBottomSheeet.HIDE
                },
                sheetState = sheetState,
            ) {
                // Sheet content
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier =
                        Modifier
                            .padding(15.dp)
                            .wrapContentHeight()
                            .fillMaxWidth(),
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = stringResource(resource = Res.string.validator_label),
                        tint = md_theme_light_primary,
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = md_theme_light_primary,
                        text =
                            if (showBottomSheet == ValidatorBottomSheeet.VALIDATOR) {
                                stringResource(resource = Res.string.validator_label)
                            } else if (showBottomSheet == ValidatorBottomSheeet.APY) {
                                stringResource(resource = Res.string.validator_est_apy_long)
                            } else {
                                ""
                            },
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier =
                        Modifier
                            .padding(15.dp)
                            .wrapContentHeight()
                            .fillMaxWidth(),
                ) {
                    Text(
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = md_theme_light_primary,
                        text =
                            if (showBottomSheet == ValidatorBottomSheeet.VALIDATOR) {
                                stringResource(resource = Res.string.validator_label_desc)
                            } else if (showBottomSheet == ValidatorBottomSheeet.APY) {
                                stringResource(resource = Res.string.validator_est_apy_desc)
                            } else {
                                ""
                            },
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier =
                        Modifier
                            .padding(15.dp)
                            .wrapContentHeight()
                            .fillMaxWidth(),
                ) {
                    Button(
                        onClick = {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = ValidatorBottomSheeet.HIDE
                                }
                            }
                        },
                    ) {
                        Text("Done")
                    }
                }
            }
        }
    }
}

private val placeHolderValidatorList =
    listOf(
        ValidatorEntity(
            id = 1,
            name = "reti.algo",
            algoStaked = 25000,
            apy = 3.03,
            percentToValidator = 5,
            epochRoundLength = 1296,
            minEntryStake = 5,
        ),
        ValidatorEntity(
            id = 2,
            name = "test2.algo",
            algoStaked = 25000,
            apy = 3.03,
            percentToValidator = 5,
            epochRoundLength = 1296,
            minEntryStake = 5,
        ),
    )

private enum class ValidatorBottomSheeet {
    HIDE,
    VALIDATOR,
    APY,
}

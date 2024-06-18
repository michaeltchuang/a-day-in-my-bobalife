package com.michaeltchuang.example.ui.widgets

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.michaeltchuang.example.data.local.entities.ValidatorEntity
import com.michaeltchuang.example.data.models.Validator
import com.michaeltchuang.example.ui.viewmodels.ValidatorsListUIState
import com.michaeltchuang.example.ui.viewmodels.ValidatorsListViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@SuppressLint("ComposableNaming")
@Composable
fun SearchableToolbar(
    onPlayerSelected: (validator: Validator) -> Unit,
    onShowSettings: () -> Unit,
) {
    val validatorsListViewModel = koinViewModel<ValidatorsListViewModel>()
    val validatorListUIState = validatorsListViewModel.validatorsListUIState.collectAsStateWithLifecycle()

    val allValidators = validatorsListViewModel.validatorsFromDb.collectAsStateWithLifecycle()
    val validatorSearchQuery = validatorsListViewModel.searchQuery.collectAsStateWithLifecycle()

    Column {
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
                LazyColumn {
                    items(
                        items = placeHolderValidatorList,
                        itemContent = { validator ->
                            ListItem(validator)
                        },
                    )
                }
            }

            is ValidatorsListUIState.Success -> {
                LazyColumn {
                    items(
                        items = uiState.result,
                        itemContent = { validator ->
                            ListItem(validator)
                        },
                    )
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
            apr = 3.03,
            percentToValidator = 5,
            epochRoundLength = 1296,
            minEntryStake = 5,
        ),
        ValidatorEntity(
            id = 2,
            name = "test2.algo",
            algoStaked = 25000,
            apr = 3.03,
            percentToValidator = 5,
            epochRoundLength = 1296,
            minEntryStake = 5,
        ),
    )

package com.michaeltchuang.example.ui.screens

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.michaeltchuang.example.ui.navigation.Screen
import com.michaeltchuang.example.ui.viewmodels.AlgorandBaseViewModel
import com.michaeltchuang.example.ui.viewmodels.ValidatorSearchViewModel
import com.michaeltchuang.example.ui.widgets.ValidatorSearchWidget
import org.koin.compose.getKoin

@SuppressLint("ComposableNaming")
@Composable
fun ValidatorSearchScreen(
    navController: NavController,
    algorandBaseViewModel: AlgorandBaseViewModel,
    tag: String,
) {

    val validatorSearchViewModel: ValidatorSearchViewModel = getKoin().get()
    validatorSearchViewModel.algorandBaseViewModel = algorandBaseViewModel

    ValidatorSearchWidget(
        onValidatorSelected = { validatorId ->
            navController.navigate(Screen.ValidatorDetailScreen.route + "/${validatorId}")
        },
    )
}

package com.michaeltchuang.example.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.michaeltchuang.example.ui.viewmodels.HomeViewModel
import com.michaeltchuang.example.ui.widgets.PiProgressIndicator
import com.michaeltchuang.example.ui.widgets.ProductCard
import org.koin.compose.getKoin

@Composable
fun ValidatorsListScreen(navController: NavHostController) {
    val viewModel: HomeViewModel = getKoin().get()
    val homeScreenState by viewModel.homeViewState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getProducts()
    }
    when (homeScreenState) {
        is HomeViewModel.HomeScreenState.Loading -> {
            PiProgressIndicator()
        }
        is HomeViewModel.HomeScreenState.Success -> {
            val products = (homeScreenState as HomeViewModel.HomeScreenState.Success).responseData.list
            ProductCard(products)
        }
        is HomeViewModel.HomeScreenState.Error -> {
            // show Error
        }
    }
}

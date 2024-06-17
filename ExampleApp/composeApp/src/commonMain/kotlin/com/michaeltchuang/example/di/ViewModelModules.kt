package com.michaeltchuang.example.di

import com.michaeltchuang.example.ui.viewmodels.AccountViewModel
import com.michaeltchuang.example.ui.viewmodels.BaseViewModel
import com.michaeltchuang.example.ui.viewmodels.LoginViewModel
import com.michaeltchuang.example.ui.viewmodels.PlayCoinFlipperViewModel
import com.michaeltchuang.example.ui.viewmodels.ValidatorsListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val provideViewModelModules =
    module {
        viewModel { AccountViewModel(get()) }
        viewModel { BaseViewModel(get()) }
        viewModel { LoginViewModel(get()) }
        viewModel { PlayCoinFlipperViewModel(get()) }
        viewModel { ValidatorsListViewModel(get()) }
    }

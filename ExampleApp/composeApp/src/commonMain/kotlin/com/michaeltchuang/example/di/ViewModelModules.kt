package com.michaeltchuang.example.di

import com.michaeltchuang.example.ui.viewmodels.AlgorandBaseViewModel
import com.michaeltchuang.example.ui.viewmodels.PlayCoinFlipperViewModel
import com.michaeltchuang.example.ui.viewmodels.ValidatorDetailViewModel
import com.michaeltchuang.example.ui.viewmodels.ValidatorsListViewModel
import org.koin.dsl.module

val provideViewModelModules =
    module {
        single { AlgorandBaseViewModel(get()) }
        single { PlayCoinFlipperViewModel(get()) }
        single { ValidatorsListViewModel(get()) }
        single { ValidatorDetailViewModel(get()) }
    }

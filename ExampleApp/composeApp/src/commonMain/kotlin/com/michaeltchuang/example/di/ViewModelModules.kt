package com.michaeltchuang.example.di

import com.michaeltchuang.example.ui.viewmodels.AlgorandBaseViewModel
import com.michaeltchuang.example.ui.viewmodels.PlayCoinFlipperViewModel
import com.michaeltchuang.example.ui.viewmodels.ValidatorDetailViewModel
import com.michaeltchuang.example.ui.viewmodels.ValidatorSearchViewModel
import org.koin.dsl.module

val provideViewModelModules =
    module {
        single { AlgorandBaseViewModel(get()) }
        single { PlayCoinFlipperViewModel(get()) }
        single { ValidatorSearchViewModel(get()) }
        single { ValidatorDetailViewModel(get()) }
    }

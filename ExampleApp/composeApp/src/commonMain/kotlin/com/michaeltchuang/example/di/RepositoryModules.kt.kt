package com.michaeltchuang.example.di

import com.michaeltchuang.example.data.repositories.AlgorandRepository
import com.michaeltchuang.example.data.repositories.CoinFlipperRepository
import com.michaeltchuang.example.data.repositories.ValidatorRepository
import org.koin.dsl.module

val provideRepositoryModules =
    module {
        single<AlgorandRepository> { AlgorandRepository() }
        single<CoinFlipperRepository> { CoinFlipperRepository() }
        single<ValidatorRepository> { ValidatorRepository(get()) }
    }

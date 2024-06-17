package com.michaeltchuang.example.di

import com.michaeltchuang.example.data.repositories.AlgorandRepository
import com.michaeltchuang.example.data.repositories.CoinFlipperRepository
import com.michaeltchuang.example.data.repositories.ValidatorRepository
import com.michaeltchuang.example.ui.viewmodels.BaseViewModel
import com.michaeltchuang.example.ui.viewmodels.LoginViewModel
import com.michaeltchuang.example.ui.viewmodels.PlayCoinFlipperViewModel
import com.michaeltchuang.example.ui.viewmodels.ValidatorsListViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val provideHttpClientModule =
    module {
        single {
            HttpClient {
                install(ContentNegotiation) {
                    json(json = Json { ignoreUnknownKeys = true }, contentType = ContentType.Any)
                }
            }
        }
    }

val provideRepositoryModule =
    module {
        single<AlgorandRepository> { AlgorandRepository() }
        single<CoinFlipperRepository> { CoinFlipperRepository() }
        single<ValidatorRepository> { ValidatorRepository() }
    }

val provideViewModelModule =
    module {
        single { BaseViewModel(get()) }
        single { LoginViewModel(get()) }
        single { PlayCoinFlipperViewModel(get()) }
        single { ValidatorsListViewModel(get()) }
    }

fun appModule() = listOf(provideHttpClientModule, provideRepositoryModule, provideViewModelModule)

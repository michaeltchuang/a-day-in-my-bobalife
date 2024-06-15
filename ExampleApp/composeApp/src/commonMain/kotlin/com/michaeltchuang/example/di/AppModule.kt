package com.michaeltchuang.example.di

import com.michaeltchuang.example.data.repositories.AlgorandRepository
import com.michaeltchuang.example.data.repositories.NetworkRepository
import com.michaeltchuang.example.ui.viewmodels.BaseViewModel
import com.michaeltchuang.example.ui.viewmodels.HomeViewModel
import com.michaeltchuang.example.ui.viewmodels.LoginViewModel
import com.michaeltchuang.example.ui.viewmodels.PlayCoinFlipperViewModel
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
        single<NetworkRepository> { NetworkRepository(get()) }
    }

val provideViewModelModule =
    module {
        single { BaseViewModel(get()) }
        single { HomeViewModel(get()) }
        single { LoginViewModel(get()) }
        single { PlayCoinFlipperViewModel(get()) }
    }

fun appModule() = listOf(provideHttpClientModule, provideRepositoryModule, provideViewModelModule)

package com.michaeltchuang.example.di

import com.michaeltchuang.example.data.repositories.NetworkRepository
import com.michaeltchuang.example.ui.viewmodels.HomeViewModel
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
        single<NetworkRepository> { NetworkRepository(get()) }
    }

val provideViewModelModule =
    module {
        single {
            HomeViewModel(get())
        }
    }

fun appModule() = listOf(provideHttpClientModule, provideRepositoryModule, provideViewModelModule)

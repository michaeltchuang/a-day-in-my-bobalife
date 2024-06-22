package com.michaeltchuang.example.di

import com.michaeltchuang.example.data.network.NfdApi
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val provideHttpClientModules =
    module {
        single {
            HttpClient {
                install(ContentNegotiation) {
                    json(json = Json { ignoreUnknownKeys = true }, contentType = ContentType.Any)
                }
            }
        }
        single { NfdApi(get()) }
    }

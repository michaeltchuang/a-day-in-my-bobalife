package com.michaeltchuang.example.di

import com.michaeltchuang.example.data.local.AppSettings
import com.michaeltchuang.example.providePlatformModules
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun appModules() =
    listOf(
        provideHttpClientModules,
        provideRepositoryModules,
        provideViewModelModules,
        providePlatformModules(), // Room DB & DataStore located here
        module {
            single { AppSettings(get()) }
        },
    )

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(appModules())
    }

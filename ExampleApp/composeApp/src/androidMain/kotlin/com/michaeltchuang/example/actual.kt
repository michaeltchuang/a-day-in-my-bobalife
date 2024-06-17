package com.michaeltchuang.example

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.michaeltchuang.example.data.local.createDataStore
import io.ktor.client.engine.android.Android
import org.koin.dsl.module

actual fun providePlatformModules() =
    module {
        single { Android.create() }
        single { provideDataStore(get()) }

        // single<ExampleDatabase> { provideRoomDatabase(get()) }
    }

// fun provideRoomDatabase(ctx: Context): ExampleDatabase {
//    val dbFile = ctx.getDatabasePath(dbFileName)
//    return Room.databaseBuilder<ExampleDatabase>(ctx, dbFile.absolutePath)
//        .setDriver(BundledSQLiteDriver())
//        .setQueryCoroutineContext(Dispatchers.IO)
//        .build()
// }

fun provideDataStore(context: Context): DataStore<Preferences> =
    createDataStore(
        producePath = { context.filesDir.resolve("fpl.preferences_pb").absolutePath },
    )

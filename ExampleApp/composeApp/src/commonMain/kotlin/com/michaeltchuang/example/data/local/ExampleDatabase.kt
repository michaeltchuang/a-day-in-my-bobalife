package com.michaeltchuang.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.michaeltchuang.example.data.local.dao.ExampleDao
import com.michaeltchuang.example.data.local.entities.ValidatorEntity

@Database(entities = [ValidatorEntity::class], version = 2, exportSchema = false)
abstract class ExampleDatabase : RoomDatabase() {
    abstract fun getDao(): ExampleDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ExampleDatabase? = null

        fun getDatabase(context: Context): ExampleDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance =
                    Room
                        .databaseBuilder(
                        context.applicationContext,
                        ExampleDatabase::class.java,
                        "dbMp",
                    ).fallbackToDestructiveMigration(true).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}

internal const val dbFileName = "exampledatabase.db"

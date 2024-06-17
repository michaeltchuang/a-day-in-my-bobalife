package com.michaeltchuang.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.michaeltchuang.example.data.local.entities.ValidatorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExampleDao {
    @Query("SELECT * FROM validators ORDER BY id desc")
    fun getAllValidatorsAsFlow(): Flow<List<ValidatorEntity>>

    @Query("SELECT * FROM validators WHERE id = :id")
    suspend fun getValidatorById(id: Int): ValidatorEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertValidator(validator: ValidatorEntity): Long

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertValidatorNotSuspend(validator: ValidatorEntity): Long
}

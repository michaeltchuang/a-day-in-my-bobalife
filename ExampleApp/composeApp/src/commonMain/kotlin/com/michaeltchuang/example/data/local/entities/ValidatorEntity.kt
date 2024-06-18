package com.michaeltchuang.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "validators")
data class ValidatorEntity(
    @PrimaryKey() val id: Int = 0,
    val name: String,
    val algoStaked: Int?,
    val apr: Double? = null,
    val percentToValidator: Int?,
    val epochRoundLength: Int?,
    val minEntryStake: Int?,
)

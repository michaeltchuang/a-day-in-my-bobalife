package com.michaeltchuang.example.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Validator(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("algoStaked")
    val algoStaked: Int?,
    @SerialName("apy")
    val apy: Double,
    @SerialName("percentToValidator")
    val percentToValidator: Int,
    @SerialName("epochRoundLength")
    val epochRoundLength: Int,
    @SerialName("minEntryFee")
    val minEntryStake: Int,
    // val config: ValidatorConfig,
    // val state: ValidatorState,
    // val pools: PoolInfo[],
    // val nodePoolAssignment: NodePoolAssignmentConfig,
    // val rewardToken?: Asset,
    // val gatingAssets?: Asset[],
    // val nfd: Nfd?,
    // val apy: Int?,
)

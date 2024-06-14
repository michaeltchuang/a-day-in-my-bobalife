package com.algorand.example.coinflipper.data.model

data class Validator(
    val id: Int,
    val config: ValidatorConfig,
    // val state: ValidatorState,
    // val pools: PoolInfo[],
    // val nodePoolAssignment: NodePoolAssignmentConfig,
    // val rewardToken?: Asset,
    // val gatingAssets?: Asset[],
    // val nfd: Nfd?,
    // val apy: Int?,
)

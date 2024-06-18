package com.michaeltchuang.example.data.model

import java.math.BigInteger

data class ValidatorConfig(
    val id: Int, // id of this validator (sequentially assigned)
    val owner: String, // account that controls config - presumably cold-wallet
    val manager: String, // account that triggers/pays for payouts and keyreg transactions - needs to be hotwallet
    val nfdForInfo: Int,
    val entryGatingType: Int,
    val entryGatingAddress: String,
    // val entryGatingAssets: EntryGatingAssets,
    val gatingAssetMinBalance: BigInteger,
    val rewardTokenId: Int,
    val rewardPerPayout: BigInteger,
    val epochRoundLength: Int, // Epoch length in rounds
    val percentToValidator: Int, // Payout percentage expressed w/ four decimals - ie: 50000 = 5% -> .0005 -
    val validatorCommissionAddress: String, // account that receives the validation commission each epoch payout (can be ZeroAddress)
    val minEntryStake: BigInteger, // minimum stake required to enter pool - but must withdraw all if they want to go below this amount
    val maxAlgoPerPool: BigInteger, // maximum stake allowed per pool (to keep under incentive limits)
    val poolsPerNode: Int, // Number of pools to allow per node (max of 3 is recommended)
    val sunsettingOn: Int, // timestamp when validator will sunset (if != 0)
    val sunsettingTo: Int, // validator id that validator is 'moving' to (if known)
)

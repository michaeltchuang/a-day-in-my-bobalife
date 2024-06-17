package com.michaeltchuang.example.ui.viewmodels

import android.app.GameState
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.michaeltchuang.example.data.repositories.CoinFlipperRepository
import com.michaeltchuang.example.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigInteger

class PlayCoinFlipperViewModel(
    private val repository: CoinFlipperRepository,
) : BaseViewModel(repository) {
    override val TAG = "PlayCoinFlipperViewModel"

    enum class GameState {
        BET,
        PENDING,
        SETTLE,
    }

    var _appGameState = MutableStateFlow<GameState>(GameState.BET)
    var appGameStateFlow = _appGameState.asStateFlow()
    var currentGameState: GameState = GameState.BET

    var _snackBarMessage = MutableStateFlow<String>("")
    var snackBarStateFlow = _snackBarMessage.asStateFlow()

    var _currentRound = MutableStateFlow<Long?>(null)
    var currentRoundStateFlow = _currentRound.asStateFlow()
    var currentRound = 0L

    var betMicroAlgosAmount = Constants.DEFAULT_MICRO_ALGO_BET_AMOUNT
    var isHeads = true
    var contract = ""

    fun hasExistingBetState() {
        accountInfo?.appsLocalState?.firstOrNull({ it.id == Constants.COINFLIP_APP_ID_TESTNET })
            .let { applicationLocalState ->
                // first matching app id
                // Log.d(TAG, "User's application local state: ${applicationLocalState?.keyValue.toString()}")
                applicationLocalState?.keyValue?.forEach {
                    when (it.key) {
                        Constants.COINFLIP_APP_COMMITTMENT_ROUND_KEY -> {
                            if (commitmentRound == 0L) {
                                commitmentRound = it.value.uint.toLong()
                            }
                            // Log.d(TAG, "User's application local state: ${it}")
                        }
                    }
                }
            }
        if (currentRound < (commitmentRound + 10L)) {
            currentGameState = GameState.PENDING
            getCurrentRound()
        } else if (currentRound == 0L || commitmentRound == 0L) {
            // skip if network error
        } else {
            currentGameState = GameState.SETTLE
        }
        _appGameState.value = currentGameState
    }

    fun updateGameState() {
        viewModelScope.launch {
            when (currentGameState) {
                GameState.BET -> {
                    val acct = accountStateFlow.value
                    acct?.apply {
                        val result = repository.appFlipCoin(acct, Constants.COINFLIP_APP_ID_TESTNET, contract, betMicroAlgosAmount, isHeads)
                        if (result?.confirmedRound != null) {
                            commitmentRound = (result.methodResults.get(0).value as BigInteger).toLong()
                            hasExistingBet = true
                            currentGameState = GameState.PENDING
                        } else {
                            _snackBarMessage.value = "Could not submit bet on chain.  Please check logs for issue"
                            currentGameState = GameState.BET
                        }
                    }
                }
                GameState.PENDING -> {
                    // currentGameState = GameState.SETTLE
                    // settle button is not clickable when pending
                }
                GameState.SETTLE -> {
                    val acct = accountStateFlow.value
                    acct?.apply {
                        val result =
                            repository.appSettleBet(
                                acct,
                                Constants.COINFLIP_APP_ID_TESTNET,
                                contract,
                                BigInteger.valueOf(Constants.RANDOM_BEACON_APPID),
                            )
                        if (result?.confirmedRound == null) {
                            _snackBarMessage.value = "Could not settle bet on chain.  Please check logs for issue"
                        } else if (result.methodResults == null) {
                            _snackBarMessage.value = "Unexpected server response.  Please check logs for detail"
                        } else {
                            // successful result
                            val betResult = result.methodResults?.get(0)?.value as Array<*>
                            _snackBarMessage.value = createAlertMessage(betResult)
                            resetGame()
                        }
                    }
                }
            }
            _appGameState.value = currentGameState
        }
    }

    fun createAlertMessage(result: Array<*>): String {
        val outcome = if (result.get(0) as Boolean == true) "Won!" else "Lost :("
        val amount = result.get(1) as BigInteger
        val msg = "You $outcome  ($amount)"
        return(msg)
    }

    fun getCurrentRound() {
        viewModelScope.launch {
            val acct = accountStateFlow.value
            acct?.apply {
                val round = repository.getCurrentRound(acct, Constants.COINFLIP_APP_ID_TESTNET)
                Log.d(TAG, "Current Round: $round")
                currentRound = round
                _currentRound.value = round
            }
        }
    }

    fun calculateProgress(): Float {
        val targetRound = commitmentRound + 10L
        if (commitmentRound == 0L) {
            return 0.0f
        } else if (currentRound >= targetRound) {
            return 1.0f
        } else {
            val diff = targetRound - currentRound
            Log.d(TAG, "$targetRound $currentRound $diff ${(diff / 10.0f)}")
            return (10.0f - diff) / 10.0f
        }
    }

    fun resetGame() {
        currentGameState = GameState.BET
        betMicroAlgosAmount = Constants.DEFAULT_MICRO_ALGO_BET_AMOUNT
        commitmentRound = 0L
        hasExistingBet = false
    }
}

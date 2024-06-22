package com.michaeltchuang.example.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michaeltchuang.example.data.repositories.CoinFlipperRepository
import com.michaeltchuang.example.utils.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigInteger

class PlayCoinFlipperViewModel(
    private val repository: CoinFlipperRepository,
) : ViewModel() {
    val TAG = "PlayCoinFlipperViewModel"

    var algorandBaseViewModel: AlgorandBaseViewModel? = null

    enum class GameState {
        BET,
        PENDING,
        SETTLE,
        OTHER,
    }

    var _appGameState = MutableStateFlow<GameState>(GameState.OTHER)
    var appGameStateFlow = _appGameState.asStateFlow()

    var _currentRound = MutableStateFlow<Long>(0L)
    var currentRoundStateFlow = _currentRound.asStateFlow()

    var _snackbarMessage = MutableStateFlow<String>("")
    var snackBarMessageStateFlow = _snackbarMessage.asStateFlow()

    var commitmentRound = 0L

    var betMicroAlgosAmount = Constants.DEFAULT_MICRO_ALGO_BET_AMOUNT
    var isHeads = true
    var abiContract = "CoinFlipper.arc4.json"

    fun hasExistingBetState() {
        algorandBaseViewModel
            ?.accountInfo
            ?.appsLocalState
            ?.firstOrNull({ it.id == Constants.COINFLIP_APP_ID_TESTNET })
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
        if (_currentRound.value < (commitmentRound + 10L)) {
            _appGameState.value = GameState.PENDING
            getCurrentRound()
        } else if (_currentRound.value == 0L || commitmentRound == 0L) {
            // skip if network error
        } else {
            _appGameState.value = GameState.SETTLE
        }
    }

    fun updateGameState() {
        viewModelScope.launch {
            when (_appGameState.value) {
                GameState.BET -> {
                    val acct = algorandBaseViewModel?.account
                    acct?.apply {
                        val result =
                            repository.appFlipCoin(
                                acct,
                                abiContract,
                                betMicroAlgosAmount,
                                isHeads,
                            )
                        if (result?.confirmedRound != null) {
                            commitmentRound = (result.methodResults.get(0).value as BigInteger).toLong()
                            algorandBaseViewModel?.hasExistingBet = true
                            _appGameState.value = GameState.PENDING
                        } else {
                            setSnackBarMessage("Could not submit bet on chain.  Please check logs for issue")
                            _appGameState.value = GameState.BET
                        }
                    }
                }
                GameState.PENDING -> {
                    // currentGameState = GameState.SETTLE
                    // settle button is not clickable when pending
                }
                GameState.SETTLE -> {
                    val acct = algorandBaseViewModel?.account
                    acct?.apply {
                        val result =
                            repository.appSettleBet(
                                acct,
                                abiContract,
                            )
                        if (result == null) {
                            setSnackBarMessage("Could not settle bet on chain.  Please check logs for issue")
                        } else {
                            launch {
                                setSnackBarMessage(result)
                            }
                            resetGame()
                        }
                    }
                } GameState.OTHER -> {
                    if (algorandBaseViewModel?.hasExistingBet!!) {
                        hasExistingBetState()
                    } else {
                        // Clicked Bet Form
                        val acct = algorandBaseViewModel?.account
                        acct?.apply {
                            val result =
                                repository.appFlipCoin(
                                    acct,
                                    abiContract,
                                    betMicroAlgosAmount,
                                    isHeads,
                                )
                            if (result?.confirmedRound != null) {
                                _currentRound.value = repository.getCurrentRound()
                                commitmentRound = (result.methodResults.get(0).value as BigInteger).toLong()
                                algorandBaseViewModel?.hasExistingBet = true
                                _appGameState.value = GameState.PENDING
                            } else {
                                algorandBaseViewModel?._snackBarMessage?.value =
                                    "Could not submit bet on chain.  Please check logs for issue"
                                _appGameState.value = GameState.BET
                            }
                        }
                    }
                }
            }
        }
    }

    fun setSnackBarMessage(str: String) {
        _snackbarMessage.value = str
        viewModelScope.launch {
            delay(3000L)
            _snackbarMessage.value = ""
        }
    }

    fun getCurrentRound() {
        viewModelScope.launch {
            val acct = algorandBaseViewModel?.account
            acct?.apply {
                val round = repository.getCurrentRound()
                Log.d(TAG, "Current Round: $round")
                _currentRound.value = round
            }
        }
    }

    fun calculateProgress(): Float {
        val targetRound = commitmentRound + 10L
        if (commitmentRound == 0L) {
            return 0.0f
        } else if (_currentRound.value >= targetRound) {
            return 1.0f
        } else {
            val diff = targetRound - _currentRound.value
            Log.d(TAG, "$targetRound ${_currentRound.value} $diff ${(diff / 10.0f)}")
            return (10.0f - diff) / 10.0f
        }
    }

    fun resetGame() {
        _appGameState.value = GameState.BET
        betMicroAlgosAmount = Constants.DEFAULT_MICRO_ALGO_BET_AMOUNT
        commitmentRound = 0L
        algorandBaseViewModel?.hasExistingBet = false
    }
}
